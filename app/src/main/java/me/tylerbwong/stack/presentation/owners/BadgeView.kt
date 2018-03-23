package me.tylerbwong.stack.presentation.owners

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import me.tylerbwong.stack.data.model.BadgeCounts

class BadgeView : View {

    private val goldPaint by lazy { Paint().apply { color = Color.YELLOW } }
    private val silverPaint by lazy { Paint().apply { color = Color.GRAY } }
    private val bronzePaint by lazy { Paint().apply { color = Color.BLUE } }
    private val textPaint by lazy { Paint().apply { color = Color.BLACK } }

    var badgeCounts: BadgeCounts? = null
        set(value) {
            field = value
            invalidate()
        }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    init {
        setWillNotDraw(false)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var x = 0f
        val badgeRadius = width / 8f

        if (badgeCounts?.gold != 0) {
            // draw gold badge with count at left most spot
            canvas?.drawCircle(badgeRadius, x, badgeRadius, goldPaint)
            x += badgeRadius
            canvas?.drawText(badgeCounts?.gold.toString(), x, 0f, textPaint)
            x += badgeRadius
        }

        if (badgeCounts?.silver != 0) {
            // draw silver badge with count at left most spot
            canvas?.drawCircle(badgeRadius, x, badgeRadius, silverPaint)
            x += badgeRadius
            canvas?.drawText(badgeCounts?.silver.toString(), x, 0f, textPaint)
            x += badgeRadius
        }

        if (badgeCounts?.bronze != 0) {
            // draw bronze badge with count at left most spot
            canvas?.drawCircle(badgeRadius, x, badgeRadius, bronzePaint)
            x += badgeRadius
            canvas?.drawText(badgeCounts?.bronze.toString(), x, 0f, textPaint)
            x += badgeRadius
        }
    }
}