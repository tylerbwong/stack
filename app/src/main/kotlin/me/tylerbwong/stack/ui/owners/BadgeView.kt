package me.tylerbwong.stack.ui.owners

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import me.tylerbwong.stack.R
import me.tylerbwong.stack.api.model.BadgeCounts

class BadgeView : View {

    // measurements
    private val iconHeight =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ICON_HEIGHT, context.resources.displayMetrics)
    private val iconRadius = iconHeight / 2f
    private val startEndPadding =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, context.resources.displayMetrics)
    private val iconLabelPadding =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ICON_LABEL_PADDING, context.resources.displayMetrics)
    private val badgePadding =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, BADGE_PADDING, context.resources.displayMetrics)

    // colors
    private val iconColors = listOf(
        ContextCompat.getColor(context, R.color.goldBadgeColor),
        ContextCompat.getColor(context, R.color.silverBadgeColor),
        ContextCompat.getColor(context, R.color.bronzeBadgeColor)
    )

    // paint
    private val iconPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.colorTextPrimary)
        textSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            TEXT_SIZE,
            context.resources.displayMetrics
        )
        typeface = Typeface.DEFAULT
    }
    private val textHeight = textPaint.fontMetrics.bottom - textPaint.fontMetrics.top

    // extras
    private val labelHelperRect = Rect()

    // badge data
    var badgeCounts: BadgeCounts? = null
        set(value) {
            field = value
            requestLayout()
            invalidate()
        }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    @Suppress("MagicNumber") // Only used for preview
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        if (isInEditMode) {
            badgeCounts = BadgeCounts(80, 3, 4)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        var idealWidth = paddingLeft + paddingRight + suggestedMinimumWidth + 0f
        val idealHeight = paddingTop + paddingBottom + suggestedMinimumHeight + textHeight

        badgeCounts?.let {
            var addPadding = false
            var badgeCount = 0

            // add space for each badge
            listOf(it.gold, it.silver, it.bronze).forEach { count ->
                if (count > 0) {
                    idealWidth += iconHeight + iconLabelPadding + textPaint.measureText(count.toString())
                    addPadding = true
                    badgeCount += 1
                }
            }

            // add padding between badges
            if (badgeCount >= 2) {
                idealWidth += badgePadding * (badgeCount - 1)
            }

            // add start and end padding
            if (addPadding) {
                idealWidth += startEndPadding * 2
            }
        }

        setMeasuredDimension(
            resolveSize(idealWidth.toInt(), widthMeasureSpec),
            resolveSize(idealHeight.toInt(), heightMeasureSpec)
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        badgeCounts?.let {
            val canvasHeight = height
            val centerY = canvasHeight / 2f
            var posX = paddingStart + startEndPadding

            listOf(it.gold, it.silver, it.bronze).forEachIndexed { index, amount ->
                if (amount > 0) {
                    // draw icon
                    posX += iconRadius
                    iconPaint.color = iconColors[index]
                    canvas.drawCircle(posX, centerY, iconRadius, iconPaint)

                    // draw label
                    val amountString = amount.toString()
                    posX += iconRadius + iconLabelPadding
                    textPaint.getTextBounds(amountString, 0, amountString.length, labelHelperRect)
                    canvas.drawText(
                        amountString,
                        posX,
                        canvasHeight - paddingBottom - textPaint.fontMetrics.bottom,
                        textPaint
                    )
                    posX += textPaint.measureText(amountString) + badgePadding
                }
            }
        }
    }

    companion object {
        private const val ICON_HEIGHT = 7f
        private const val ICON_LABEL_PADDING = 3f
        private const val BADGE_PADDING = 6f
        private const val TEXT_SIZE = 12f
    }
}
