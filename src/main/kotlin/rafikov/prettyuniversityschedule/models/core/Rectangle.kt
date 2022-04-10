package rafikov.prettyuniversityschedule.models.core

/**
 *
 * @author Aydar Rafikov
 */
class Rectangle(var left: Int, var top: Int, var width: Int, var height: Int) {

    val right
        get() = left + width
    val bottom
        get() = top + height
}