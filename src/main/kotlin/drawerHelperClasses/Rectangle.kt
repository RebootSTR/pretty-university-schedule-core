package drawerHelperClasses

/**
 *
 * @author Aydar Rafikov
 */
class Rectangle(var left: Int, var top: Int, var width: Int, var height: Int) {

    val right = left + width
    val bottom = top + height
}