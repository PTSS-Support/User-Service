package org.ptss.support.common.pagination

import org.eclipse.microprofile.openapi.annotations.media.Schema
import java.util.UUID

@Schema(description = "Cursor-based pagination response wrapper")
data class CursorPage<T>(
    @field:Schema(description = "List of items for the current page")
    val items: List<T>,

    @field:Schema(
        description = "Cursor for the next page. Null if this is the last page",
        example = "a829f845-b6a6-4cc6-8c62-70e75ed5e5f4"
    )
    val nextCursor: UUID?
) {
    fun <R> map(transform: (T) -> R): CursorPage<R> =
        CursorPage(items.map(transform), nextCursor)

    companion object {
        fun <T> empty() = CursorPage<T>(emptyList(), null)
    }
}

// Extension functions
inline fun <T, R> CursorPage<T>.mapItems(crossinline transform: (T) -> R): CursorPage<R> =
    CursorPage(items.map { transform(it) }, nextCursor)

fun <T> List<T>.toCursorPage(nextCursor: UUID? = null) = CursorPage(this, nextCursor)