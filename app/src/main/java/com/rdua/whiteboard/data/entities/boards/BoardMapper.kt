package com.rdua.whiteboard.data.entities.boards

import auto_map.internal.AutoMapProcessor
import auto_map.internal.toMap
import com.rdua.whiteboard.board.model.BoardItemModel
import com.rdua.whiteboard.board.model.FrameModel
import com.rdua.whiteboard.board.model.LineModel
import com.rdua.whiteboard.board.model.ShapeModel
import com.rdua.whiteboard.board.model.StickyModel
import com.rdua.whiteboard.board.model.TextModel
import com.rdua.whiteboard.data.entities.boards.wrappers.FrameEntity
import com.rdua.whiteboard.data.entities.boards.wrappers.LineEntity
import com.rdua.whiteboard.data.entities.boards.wrappers.ShapeEntity
import com.rdua.whiteboard.data.entities.boards.wrappers.StickyEntity
import com.rdua.whiteboard.data.entities.boards.wrappers.TextEntity
import com.rdua.whiteboard.data.entities.boards.wrappers.toEntity
import com.rdua.whiteboard.data.entities.boards.wrappers.toModel
import javax.inject.Inject

/*
 * Mapping guide:
 * 1) Before storing internal types (BoardItemModel) you need to convert them to a type, supported
 * by Firebase Database.
 * 2) Firebase supports working with:
 *  - String, Boolean
 *  - Numerical types. Could be converted by firebase to a different type (e.g. 500.0f will convert to 500 Long)
 *  - enum classes - will be converted to String.
 *  - Map<String, Any?>.
 * 3) Every Model class should have an Entity data class that will copy its structure, but use types
 * supported by Firebase Database. E.g. Color class can be converted to a formatted String and back
 * manually etc.
 * 4) These Entities should then be annotated with @AutoMap for AutoMapCodeGenerator (code-gen module)
 * to generate their toMap/toEntity conversion functions.
 * 5) @AutoMap allows to annotate your Entity properties to call their toMap/toEntity during outer
 * type conversion.
 * 6) Providing a UNIQUE className to @AutoMap will allow to easily restore original entity from map.
 * It will create an additional pair in your output map `"class" to "your_unique_name"`.
 * 7) If className was provided to @AutoMap for this entity, you can use AutoMapProcessor function
 * resolveToEntity(map: Map<String, Any?>) to convert your map back to entity.
 * 8) If you don't know the specific type of the entity, you can convert to Any and then type check
 * `resolveToEntity<Any?>(yourMap)`.
 * 9) After restoring your entity from map, cast it to original BoardItemModel type. You might need
 * to do additional steps like sorting your Firebase map or providing data that wasn't store in
 * Firebase map (e.g. author name for StickyModel).
 */

class BoardMapper(
    private val autoMapProcessor: AutoMapProcessor = AutoMapProcessor(),
) {
    // AutoMapProcessor is a code generated class and cannot be injected directly by DI.
    // Creating a separate Inject constructor solves the issue.
    @Inject
    constructor() : this(autoMapProcessor = AutoMapProcessor())

    /**
     * Converts [BoardEntity] - internal entity class - to [FirebaseBoardEntity] - entity
     * representation savable in Firebase database.
     */
    fun toFirebaseEntity(entity: BoardEntity) = FirebaseBoardEntity(
        id = entity.id,
        title = entity.title,
        imageUrl = entity.imageUrl,
        creator = entity.creator,
        createdAt = entity.createdAt,
        modifiedBy = entity.modifiedBy,
        modifiedAt = entity.modifiedAt,
        content = entity.content?.let { toFirebaseBoardContent(entity.content) },
        users = entity.users,
    )

    // FirebaseBoardEntity field nullability is a requirement of Firebase.
    // If for some reason we receive null in fields that should be non-nullable, process it somehow.
    /**
     * Converts [FirebaseBoardEntity] (Board entity savable in Firebase) to [BoardEntity] used
     * internally.
     *
     * Firebase doesn't store StickyNote's author name. It has to be provided by [resolveStickyAuthor]
     * function by StickyNote creator id.
     */
    suspend fun toEntity(
        entity: FirebaseBoardEntity,
        resolveStickyAuthor: suspend (id: String) -> String? = { null },
    ) = BoardEntity(
        id = entity.id,
        title = entity.title!!,
        imageUrl = entity.imageUrl,
        creator = entity.creator!!,
        createdAt = entity.createdAt!!,
        modifiedBy = entity.modifiedBy!!,
        modifiedAt = entity.modifiedAt!!,
        content = entity.content?.toBoardContent(resolveStickyAuthor),
        users = entity.users,
    )

    /**
     * Converts internal board content to a type structure savable in Firebase database.
     */
    fun toFirebaseBoardContent(content: List<BoardItemModel>): Map<String, Map<String, Any?>> =
        content.withIndex().associate { element ->
            element.value.id to toFirebaseBoardItem(element.value, element.index)
        }

    /**
     * Essentially `Map<String, Map<String, Any?>>` represent Firebase board content - a map of
     * board item entities converted to `Map<String, Any?>`.
     *
     * This function converts Firebase board content to internal board content which is a list of
     * [BoardItemModel].
     */
    private suspend fun Map<String, Map<String, Any?>>.toBoardContent(
        resolveStickyAuthor: suspend (id: String) -> String? = { null },
    ): List<BoardItemModel> {
        // Firebase doesn't preserve Map's elements order, so we need to sort by zPosition for
        // Compose to draw elements correctly.
        return this.toSortedMap(
            compareBy { this[it]?.get("zPosition").toString().toInt() }
        ).map { element ->
            element.value.toModel(resolveStickyAuthor)
        }
    }

    /**
     * Converts [BoardItemModel] to its map representation savable in Firebase.
     *
     * Requires board item's [position] in it's list to be saved as a property to preserve original
     * item order. Firebase doesn't save items in order.
     */
    fun toFirebaseBoardItem(model: BoardItemModel, position: Int): Map<String, Any?> {
        return when (model) {
            is StickyModel -> model.toEntity(position).toMap()
            is ShapeModel -> model.toEntity(position).toMap()
            is TextModel -> model.toEntity(position).toMap()
            is FrameModel -> model.toEntity(position).toMap()
            is LineModel -> model.toEntity(position).toMap()
        }
    }

    /**
     * Converts Firebase data structure back to its original [BoardItemModel].
     *
     * Firebase doesn't store StickyNote's author name. It has to be provided by [resolveStickyAuthor]
     * function by StickyNote creator id.
     */
    private suspend fun Map<String, Any?>.toModel(
        resolveStickyAuthor: suspend (id: String) -> String? = { null }
    ): BoardItemModel {
        return when (val entity = autoMapProcessor.resolveToEntity<Any?>(this)) {
            is StickyEntity -> entity.toModel(resolveStickyAuthor)
            is ShapeEntity -> entity.toModel()
            is TextEntity -> entity.toModel()
            is FrameEntity -> entity.toModel()
            is LineEntity -> entity.toModel()

            else -> {
                // Models saved to Firebase properly should be able to convert back alright.
                throw Exception("Couldn't convert $this to model")
            }
        }
    }
}
