package reboot.prettyscheduleapp

import drawerHelperClasses.Alignment
import drawerHelperClasses.Arrangement
import drawerHelperClasses.Rectangle
import drawerHelperClasses.Typeface
import models.Day
import models.Lesson
import models.LessonType
import models.Schedule
import java.io.File

/**
 *
 * @author Aydar Rafikov
 */
abstract class CoreDrawer<Color, TextSize>(private val schedule: Schedule) {

    companion object {
        private const val CELL = 20
        private const val WIDTH = 79 * CELL
        private const val SMALL_BORDER = 2
        private const val BIG_BORDER = 5
        private const val VOID_BORDER = 1 * CELL
        private const val HEADER_HEIGHT = 4 * CELL
        private const val LESSON_LINE_HEIGHT: Int = (5.45 * CELL).toInt()
        private const val GROUP_NAME_WIDTH = (21.95 * CELL).toInt()
        private const val COPYRIGHT = "@rebootstr"
        private const val DAY_NAME_WIDTH = 6 * CELL
        private const val FONT = "Calibri"
        private const val COUNT_LESSONS_IN_EMPTY_DAY = 3
    }

    abstract val dayFontSize: TextSize
    abstract val timeFontSize: TextSize
    abstract val lessonFontSize: TextSize
    abstract val teacherFontSize: TextSize
    abstract val copyrightSize: TextSize

    abstract val remoteColor: Color
    abstract val emptyColor: Color

    abstract val practiceColor: Color
    abstract val labColor: Color
    abstract val lectureColor: Color
    abstract val teacherColor: Color
    abstract val copyrightColor: Color

    abstract val whiteColor: Color
    abstract val blackColor: Color

    abstract val paint: AbstractPaint<Color, TextSize>

    private var height = VOID_BORDER * 2 + HEADER_HEIGHT + LESSON_LINE_HEIGHT * schedule.times.size * schedule.days.size

    fun save(file: File) {
        paint.save(file)
    }

    fun drawSchedule() {
        paint.prepareToDraw(WIDTH, height, FONT)
        preparePaint()

        // background
        withColor(whiteColor) {
            drawCell(0, 0, WIDTH, height)
        }

        val width = WIDTH - VOID_BORDER * 2

        val headerX = VOID_BORDER
        val headerY = VOID_BORDER
        drawHeaders(headerX, headerY, HEADER_HEIGHT, width)

        val dayX = headerX
        val dayY = headerY + HEADER_HEIGHT
        var lastLessonCount = 0
        for (day in schedule.days.withIndex()) {
            if (day.value.isEmpty()) {
                drawEmptyDay(
                    dayX,
                    dayY + lastLessonCount * LESSON_LINE_HEIGHT,
                    LESSON_LINE_HEIGHT,
                    width,
                    day.value
                )
                lastLessonCount += COUNT_LESSONS_IN_EMPTY_DAY
            } else {
                drawDay(
                    dayX,
                    dayY + lastLessonCount * LESSON_LINE_HEIGHT,
                    LESSON_LINE_HEIGHT,
                    width,
                    day.value
                )
                lastLessonCount += day.value.lessons.size
            }
        }

        // crop
        paint.cropImageToHeight(dayY + lastLessonCount * LESSON_LINE_HEIGHT + VOID_BORDER)
    }

    private fun preparePaint() {
        paint.setColor(blackColor)
        paint.setTextSize(timeFontSize)
        paint.setAlignment(Alignment.CENTER)
        paint.setArrangement(Arrangement.CENTER)
    }

    private fun drawHeaders(x: Int, y: Int, height: Int, width: Int) {
        val evenAndOddWidth = (width - GROUP_NAME_WIDTH) / 2

        drawBoldString(schedule.group, Rectangle(x, y, GROUP_NAME_WIDTH, height), BIG_BORDER)
        drawString(
            "ЧЕТ",
            Rectangle(x + GROUP_NAME_WIDTH, y, evenAndOddWidth, height),
            BIG_BORDER
        )
        drawString(
            "НЕЧ",
            Rectangle(x + GROUP_NAME_WIDTH + evenAndOddWidth, y, evenAndOddWidth, height),
            BIG_BORDER
        )
    }

    private fun drawEmptyDay(x: Int, y: Int, lineHeight: Int, width: Int, day: Day) {

        // background
        withColor(emptyColor) {
            drawCell(x, y, width, lineHeight * COUNT_LESSONS_IN_EMPTY_DAY)
        }

        // global border
        drawBorder(Rectangle(x, y, width, lineHeight * COUNT_LESSONS_IN_EMPTY_DAY), BIG_BORDER)

        // day name
        drawBoldString(
            day.dayName,
            Rectangle(x, y, DAY_NAME_WIDTH, lineHeight * COUNT_LESSONS_IN_EMPTY_DAY),
            BIG_BORDER,
            true,
            size = dayFontSize
        )

        // times
        val timeWidth = GROUP_NAME_WIDTH - DAY_NAME_WIDTH
        val timeX = x + DAY_NAME_WIDTH
        drawBorder(
            Rectangle(timeX, y, timeWidth, lineHeight * COUNT_LESSONS_IN_EMPTY_DAY),
            BIG_BORDER
        )

        // copyright
        withSize(copyrightSize) {
            drawString(
                COPYRIGHT,
                Rectangle(
                    timeX + timeWidth,
                    y + lineHeight,
                    (width - DAY_NAME_WIDTH - timeWidth),
                    lineHeight
                ),
                color = copyrightColor
            )
        }
    }

    private fun drawDay(x: Int, y: Int, lineHeight: Int, width: Int, day: Day) {

        // background
        if (day.remote) {
            withColor(remoteColor) {
                drawCell(x, y, width, lineHeight * schedule.times.size)
            }
        }

        // global border
        drawBorder(Rectangle(x, y, width, lineHeight * schedule.times.size), BIG_BORDER)

        // day name
        drawBoldString(
            day.dayName,
            Rectangle(x, y, DAY_NAME_WIDTH, lineHeight * schedule.times.size),
            BIG_BORDER,
            true,
            size = dayFontSize
        )

        // times
        val timeWidth = GROUP_NAME_WIDTH - DAY_NAME_WIDTH
        val timeX = x + DAY_NAME_WIDTH
        for (i in schedule.times.indices) {
            drawString(
                schedule.times[i],
                Rectangle(timeX, y + i * lineHeight, timeWidth, lineHeight)
            )
        }
        drawBorder(Rectangle(timeX, y, timeWidth, lineHeight * schedule.times.size), BIG_BORDER)

        // lessons
        val lessonWidth = (width - DAY_NAME_WIDTH - timeWidth) / 2
        val lesson1X = timeX + timeWidth
        val lesson2X = lesson1X + lessonWidth
        for (les in day.lessons.withIndex()) {
            val lY = y + les.index * lineHeight
            val fullRect = Rectangle(lesson1X, lY, lessonWidth * 2, lineHeight)
            val lesson = les.value
            when {
                lesson.isEvenOrOdd() -> {
                    drawBorder(fullRect, SMALL_BORDER)
                    lesson.even?.run {
                        drawLesson(this, Rectangle(lesson1X, lY, lessonWidth, lineHeight))
                    }
                    lesson.odd?.run {
                        drawLesson(this, Rectangle(lesson2X, lY, lessonWidth, lineHeight))
                    }
                }
                lesson.both != null -> {
                    drawLesson(lesson.both, fullRect)
                }
            }
        }
    }

    private fun drawLesson(lesson: Lesson, rect: Rectangle) {
        drawBorder(rect, SMALL_BORDER)
        drawString(lesson.name, rect, color = lesson.getColor(), size = lessonFontSize)
        val strings = lesson.teacher.split(" ".toRegex(), 2)
        drawStringInRightTopCorner(strings[0], rect, color = teacherColor, size = teacherFontSize)
        if (strings.size > 1) {
            drawStringInRightTopCorner(
                strings[1],
                rect,
                color = teacherColor,
                size = teacherFontSize,
                lineNumber = 1
            )
        }
    }

    private fun drawCell(x: Int, y: Int, width: Int, height: Int) {
        paint.fillRect(Rectangle(x, y, width, height))
    }

    private fun withColor(color: Color, action: () -> Unit) {
        val lastColor = paint.getColor()
        paint.setColor(color)
        action()
        paint.setColor(lastColor)
    }

    private fun withSize(size: TextSize, action: () -> Unit) {
        val oldSize = paint.getTextSize()
        paint.setTextSize(size)
        action()
        paint.setTextSize(oldSize)
    }

    private fun drawString(
        text: String,
        rect: Rectangle,
        border: Int = 0,
        rotated: Boolean = false,
        color: Color = blackColor,
        size: TextSize = paint.getTextSize(),
        lineNumber: Int = 0
    ) {
        if (rotated) {
            drawRotatedTextWithBorder(text, rect, border, color)
            return
        }
        if (border != 0) {
            drawBorder(rect, border)
        }
        withSize(size) {
            withColor(color) {
                val newRect = rect.also { paint.getVerticalOffsetForNewLine() * lineNumber }

                paint.drawText(text, newRect)
            }
        }
    }

    private fun drawStringInRightTopCorner(
        text: String,
        rect: Rectangle,
        color: Color = blackColor,
        size: TextSize = paint.getTextSize(),
        lineNumber: Int = 0
    ) {
        paint.setAlignment(Alignment.RIGHT)
        paint.setArrangement(Arrangement.TOP)
        drawString(
            text,
            rect,
            color = color,
            size = size,
            lineNumber = lineNumber
        )
        paint.setAlignment(Alignment.CENTER)
        paint.setArrangement(Arrangement.CENTER)
    }

    private fun drawBoldString(
        text: String,
        rect: Rectangle,
        border: Int = 0,
        rotated: Boolean = false,
        color: Color = blackColor,
        size: TextSize = paint.getTextSize()
    ) {
        paint.setTypeface(Typeface.BOLD)
        drawString(text, rect, border, rotated, color, size)
        paint.setTypeface(Typeface.NORMAL)
    }

    private fun drawRotatedTextWithBorder(
        text: String,
        rect: Rectangle,
        border: Int,
        color: Color = blackColor,
        size: TextSize = paint.getTextSize()
    ) {
        drawBorder(rect, border)

        withSize(size) {
            withColor(color) {
                paint.drawVerticalText(text, rect)
            }
        }
    }

    private fun drawBorder(rect: Rectangle, border: Int) {
        paint.setBorderStroke(border)
        paint.drawLine(
            rect.left,
            rect.top,
            rect.right,
            rect.top
        )
        paint.drawLine(
            rect.right,
            rect.top,
            rect.right,
            rect.bottom
        )
        paint.drawLine(
            rect.right,
            rect.bottom,
            rect.left,
            rect.bottom
        )
        paint.drawLine(
            rect.left,
            rect.bottom,
            rect.left,
            rect.top
        )
    }

    private fun Lesson.getColor(): Color = when (this.type) {
        LessonType.PRACTICE -> {
            practiceColor
        }
        LessonType.LAB -> {
            labColor
        }
        LessonType.LECTURE -> {
            lectureColor
        }
    }
}
