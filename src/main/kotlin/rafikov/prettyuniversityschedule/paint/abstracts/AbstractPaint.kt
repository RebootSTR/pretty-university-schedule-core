package rafikov.prettyuniversityschedule.paint.abstracts

import rafikov.prettyuniversityschedule.models.core.Alignment
import rafikov.prettyuniversityschedule.models.core.Arrangement
import rafikov.prettyuniversityschedule.models.core.Rectangle
import rafikov.prettyuniversityschedule.models.core.Typeface
import java.io.File

/**
 *
 * @author Aydar Rafikov
 */
interface AbstractPaint<Color, TextSize> {

    fun prepareToDraw(width: Int, height: Int, font: String)

    fun setColor(color: Color)

    fun getColor(): Color

    fun setTextSize(textSize: TextSize)

    fun getTextSize(): TextSize

    fun drawText(text: String, rect: Rectangle)

    fun drawVerticalText(text: String, rect: Rectangle)

    fun setAlignment(alignment: Alignment)

    fun setArrangement(arrangement: Arrangement)

    fun setTypeface(typeface: Typeface)

    fun drawLine(x1: Int, y1: Int, x2: Int, y2: Int)

    fun fillRect(rect: Rectangle)

    fun setBorderStroke(stroke: Int)

    fun cropImageToHeight(height: Int)

    fun save(file: File)

    fun getVerticalOffsetForNewLine(): Int

    fun setAlpha(alpha: Float)
}