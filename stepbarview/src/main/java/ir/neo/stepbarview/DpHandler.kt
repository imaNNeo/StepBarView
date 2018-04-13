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

    fun spToPx(ctx: Context, sp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, ctx.resources.displayMetrics)
    }

    fun pxToSp(ctx : Context, px : Int): Float {
        return px / ctx.resources.displayMetrics.scaledDensity
    }
}