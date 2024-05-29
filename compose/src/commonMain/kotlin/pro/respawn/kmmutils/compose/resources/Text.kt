package pro.respawn.kmmutils.compose.resources

import androidx.compose.runtime.Immutable
import org.jetbrains.compose.resources.StringResource
import kotlin.jvm.JvmInline

@Immutable
public sealed interface Text {

    @JvmInline
    public value class Dynamic(public val value: String) : Text

    public class Resource(
        internal val id: StringResource,
        internal vararg val args: Any
    ) : Text {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Resource) return false

            if (id != other.id) return false
            if (!args.contentEquals(other.args)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = id.hashCode()
            result = 31 * result + args.contentHashCode()
            return result
        }
    }

    public companion object {

        public operator fun invoke(value: String): Dynamic = Dynamic(value)
        public operator fun invoke(id: StringResource, vararg args: Any): Resource = Resource(id, args = args)
    }
}
