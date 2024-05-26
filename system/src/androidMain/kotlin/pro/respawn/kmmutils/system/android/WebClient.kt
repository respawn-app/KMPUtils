@file:Suppress("unused")

package pro.respawn.kmmutils.system.android

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.DownloadListener
import android.webkit.URLUtil
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebStorage
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.net.toUri

/**
 * A listener for events that happen in [WebClient]
 */
public interface WebClientListener {

    /**
     * Is triggered when whole page could not be loaded (not triggered for css, js, ads & other errors)
     */
    public fun onError(url: Uri?)

    /**
     * Triggered **once** when the page has finished loading
     */
    public fun onSuccess(url: Uri?)

    /**
     * Triggered when you call [WebClient.load]
     */
    public fun onStartedLoading(url: Uri?)

    /**
     * Triggered in the following situations:
     * 1. When attempting to load any url that is not in [WebClient.allowedHosts]
     * 2. When opening a link that is not browser-openable (i.e. mailto:)
     * 3. When attempting to open an unknown link type
     */
    public fun onForeignUrlEncountered(url: Uri, linkType: LinkType)

    /**
     * Triggered when opening a link pointing to a file, i.e. https://<blah>.pdf etc.
     */
    public fun onRequestedFileDownload(url: Uri, fileName: String, mimetype: String?)
}

/**
 * A web client that solves many problems of [WebViewClient] and provides a nice API to use.
 * **This client automatically sets JS as enabled**. You can override this behavior by calling
 * [WebSettings.setJavaScriptEnabled]. Only domains in [allowedHosts] will be opened in the webview, others will
 * trigger [WebClientListener.onForeignUrlEncountered]. Don't forget to [attach] the client in onViewCreated and
 * [detach] it in onDestroyView.
 */
@SuppressLint("SetJavaScriptEnabled")
public open class WebClient(
    private val allowedHosts: List<String?>,
) : WebViewClient(), DownloadListener {

    private var webView: WebView? = null
    private var listener: WebClientListener? = null
    public val url: String? get() = webView?.url

    public open val canGoBack: Boolean
        get() = webView?.canGoBack() ?: false

    /**
     * Refresh current page
     */
    public open fun reload() {
        webView?.reload()
    }

    public open fun load(uri: Uri) {
        webView?.loadUrl(uri.toString())
    }

    /**
     * Call this in [androidx.appcompat.app.AppCompatActivity.onSaveInstanceState] state or
     * [androidx.fragment.app.Fragment.onViewStateRestored], **if you do not save state, webView will not save it for
     * you!**
     */
    public open fun restoreState(inState: Bundle) {
        webView?.restoreState(inState)
    }

    /**
     * Call this in [androidx.fragment.app.Fragment.onSaveInstanceState] to **save webview state**. If you do not
     * call this, **webview won't save it for you!**
     */
    public open fun saveState(outState: Bundle) {
        webView?.saveState(outState)
        // TODO: Restore and save client-specific fields
    }

    /**
     * Call this in [androidx.fragment.app.Fragment.onViewCreated]
     */
    public open fun attach(
        webView: WebView,
        listener: WebClientListener? = null,
        userAgent: String? = null,
        javaScriptEnabled: Boolean = true,
    ): WebClient {
        this.webView = webView.apply {
            settings.apply {
                setJavaScriptEnabled(javaScriptEnabled)
                loadsImagesAutomatically = true
                useWideViewPort = true
                userAgent?.let { userAgentString = it }
                loadWithOverviewMode = true
                javaScriptCanOpenWindowsAutomatically = false
                setDownloadListener(this@WebClient)
            }
            webChromeClient = WebChromeClient()
            webViewClient = this@WebClient
        }
        this.listener = listener
        return this
    }

    /**
     * Call this in [androidx.fragment.app.Fragment.onDestroyView]. If you do not call this, you will get crashes in
     * runtime and a memory leak!
     */
    public open fun detach() {
        webView = null
        listener = null
    }

    public open fun goBack() {
        if (canGoBack) webView?.goBack()
    }

    public open fun clearHistory() {
        webView?.clearHistory()
    }

    public open fun clearAllData() {
        webView?.clearHistory()
        webView?.clearCache(true)
        webView?.clearFormData()
        WebStorage.getInstance().deleteAllData()
        CookieManager.getInstance().removeAllCookies(null)
        CookieManager.getInstance().flush()
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest): Boolean {
        val uri = request.url ?: return false
        return when {
            uri.host in allowedHosts && uri.linkType == LinkType.Web -> false
            else -> {
                listener?.onForeignUrlEncountered(uri, uri.linkType)
                true
            }
        }
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        // workaround bug when onPageFinished is triggered 3 times, last one is for 100%
        if (view?.progress == 100) {
            listener?.onSuccess(url?.toUri())
        }
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url?.toUri()?.asHttps.toString(), favicon)
        listener?.onStartedLoading(url?.toUri())
    }

    // OnReceivedError indicates no connection
    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)
        // only handle the main frame because we get a bunch of errors from images, ads etc.
        if (request?.isForMainFrame == true) {
            if (request.url.scheme == "http") {
                // Try to redirect to https links if policy does not allow http.
                load(request.url.asHttps)
            } else {
                listener?.onError(request.url)
            }
        }
    }

    // when we encounter a link to a file, just open it in the browser, let the system handle it
    override fun onDownloadStart(
        url: String?,
        userAgent: String?,
        contentDisposition: String?,
        mimetype: String?,
        contentLength: Long
    ) {
        val fileName: String? = URLUtil.guessFileName(url, contentDisposition, mimetype)
        if (url?.toUri() != null && fileName != null) {
            listener?.onRequestedFileDownload(url.toUri(), fileName, mimetype)
        }
    }
}
