package pro.respawn.kmmutils.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillNode
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalAutofill
import androidx.compose.ui.platform.LocalAutofillTree
import androidx.compose.ui.platform.LocalFocusManager
import kotlinx.coroutines.launch

/**
 * Mark this element as autofillable.
 *
 * @param autofillType required first autofill type
 * @param autofillTypes additional autofill types
 * @param onFill callback to execute when the user agrees to autofill the form. Most often used to update the value
 * of the form
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Deprecated(
    """
     Use the new semantics-based Autofill APIs androidx.compose.ui.autofill.ContentType and
        androidx.compose.ui.autofill.ContentDataType instead.
"""
)
public fun Modifier.autofill(
    autofillType: AutofillType,
    vararg autofillTypes: AutofillType,
    onFill: (String) -> Unit,
): Modifier {
    val autofill = LocalAutofill.current
    val types = remember(autofillType, *autofillTypes) {
        buildList {
            add(autofillType)
            addAll(autofillTypes)
        }
    }
    val autofillNode = AutofillNode(onFill = onFill, autofillTypes = types)
    LocalAutofillTree.current += autofillNode

    return onGloballyPositioned {
        autofillNode.boundingBox = it.boundsInWindow()
    }.onFocusChanged { focusState ->
        autofill?.run {
            if (focusState.isFocused) {
                requestAutofillForNode(autofillNode)
            } else {
                cancelAutofillForNode(autofillNode)
            }
        }
    }
}

/**
 * Scrolls to this widget when an element is focused (i.e. user taps the input field).
 * Use on text input fields to keep them visible while editing.
 *
 * For this to work, wrap your scrollable container in another and add the .imePadding() modifier
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
public fun Modifier.bringIntoViewOnFocus(): Modifier {
    val coroutineScope = rememberCoroutineScope()
    val requester = remember { BringIntoViewRequester() }

    return bringIntoViewRequester(requester).onFocusEvent {
        if (it.isFocused) {
            coroutineScope.launch { requester.bringIntoView() }
        }
    }
}

/**
 * @param onOther an actions to be run when you specify one that is not the one that can be handled by focusManager
 *   e.g. Go, Search, and Send. By default does nothing.
 */
@Composable
public fun KeyboardActions.Companion.default(
    onProceed: (KeyboardActionScope.() -> Unit)? = null
): KeyboardActions = LocalFocusManager.current.run {
    remember {
        KeyboardActions(
            onDone = { clearFocus() },
            onNext = { moveFocus(FocusDirection.Next) },
            onPrevious = { moveFocus(FocusDirection.Previous) },
            onGo = onProceed,
            onSearch = onProceed,
            onSend = onProceed,
        )
    }
}
