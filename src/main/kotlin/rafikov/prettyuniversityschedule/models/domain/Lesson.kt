package rafikov.prettyuniversityschedule.models.domain

/**
 *
 * @author Aydar Rafikov
 */
class Lesson(
    val name: String,
    val type: LessonType = LessonType.LECTURE,
    val teacher: String,
    val hidden: Boolean = false
)