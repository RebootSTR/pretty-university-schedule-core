package models

/**
 *
 * @author Aydar Rafikov
 */
class Lesson(
    val name: String,
    val type: LessonType = LessonType.LECTURE,
    val teacher: String
) {
}