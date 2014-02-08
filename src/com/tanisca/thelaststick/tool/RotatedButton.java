package com.tanisca.thelaststick.tool;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.Button;

/**
 * Permet d'afficher un bouton pivot� � 180� en API7+
 * si gravity="center" est sp�cifi�, alors on applique un magic number pour
 * corriger l'affichage sinon on applique quand m�me la gravity � CENTER sans
 * correctif.
 */
public class RotatedButton extends Button {

    final boolean gravityFix;

    public RotatedButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        final int gravity = getGravity();
        if (gravity == Gravity.CENTER) {
            gravityFix = true;
        }
        else {
            setGravity(Gravity.CENTER);
            gravityFix = false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        TextPaint textPaint = getPaint();
        textPaint.setColor(getCurrentTextColor());
        textPaint.drawableState = getDrawableState();

        canvas.save();

        if (gravityFix) {
            canvas.rotate(180, this.getWidth() / 2, this.getHeight() / 2 - 3);
        }
        else {

            canvas.rotate(180, this.getWidth() / 2 + .5f, this.getHeight() / 2);
        }
        canvas.translate(getCompoundPaddingLeft(), getCompoundPaddingTop());

        getLayout().draw(canvas);
        canvas.restore();
    }
}