package ir.neo.stepbarview

import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue

/**
 * Created by iman.
 * iman.neofight@gmail.com
 */
object DpHandler {
    fun dpToPx(ctx: Context, dp: Int): Int {
        val displayMetrics = ctx.resources.displayMetrics
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }

    fun pxToDp(ctx: Context, px: Int): Int {
        val displayMetrics = ctx.resources.displayMetrics
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }

    fun spToPx(sp: Float, context: Context): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.resources.displayMetrics)
    }
}