package com.zsm.flowable.service;

import org.flowable.bpmn.model.AssociationDirection;
import org.flowable.bpmn.model.GraphicInfo;
import org.flowable.image.impl.DefaultProcessDiagramCanvas;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;


/**
 * @Author: zeng.
 * @Date:Created in 2021-02-02 17:29.
 * @Description:
 */
public class MyProcessDiagramCanvas extends DefaultProcessDiagramCanvas
{
    //设置高亮线的颜色  这里我设置成绿色
    protected static Color HIGHLIGHT_SEQUENCEFLOW_COLOR = Color.GREEN;

    //设置连接线（网关）的条件字体颜色  这里我设置成绿色
    protected static Color LABEL_COLOR = new Color(10, 176, 213);

    public MyProcessDiagramCanvas(int width, int height, int minX, int minY, String imageType, String activityFontName,
                                  String labelFontName, String annotationFontName, ClassLoader customClassLoader)
    {
        super(width, height, minX, minY, imageType, activityFontName, labelFontName, annotationFontName,
            customClassLoader);
    }

    public MyProcessDiagramCanvas(int width, int height, int minX, int minY, String imageType)
    {
        super(width, height, minX, minY, imageType);
    }

    /**
     * 画线颜色设置
     *
     * @param xPoints
     * @param yPoints
     * @param conditional
     * @param isDefault
     * @param connectionType
     * @param associationDirection
     * @param highLighted
     * @param scaleFactor
     */
    public void drawConnection(int[] xPoints, int[] yPoints, boolean conditional, boolean isDefault,
                               String connectionType, AssociationDirection associationDirection, boolean highLighted,
                               double scaleFactor)
    {

        Paint originalPaint = g.getPaint();
        Stroke originalStroke = g.getStroke();

        g.setPaint(CONNECTION_COLOR);
        if (connectionType.equals("association"))
        {
            g.setStroke(ASSOCIATION_STROKE);
        }
        else if (highLighted)
        {
            //设置线的颜色
            g.setPaint(HIGHLIGHT_SEQUENCEFLOW_COLOR);
            g.setStroke(HIGHLIGHT_FLOW_STROKE);
        }

        for (int i = 1; i < xPoints.length; i++)
        {
            Integer sourceX = xPoints[i - 1];
            Integer sourceY = yPoints[i - 1];
            Integer targetX = xPoints[i];
            Integer targetY = yPoints[i];
            Line2D.Double line = new Line2D.Double(sourceX, sourceY, targetX, targetY);
            g.draw(line);
        }

        if (isDefault)
        {
            Line2D.Double line = new Line2D.Double(xPoints[0], yPoints[0], xPoints[1], yPoints[1]);
            drawDefaultSequenceFlowIndicator(line, scaleFactor);
        }

        if (conditional)
        {
            Line2D.Double line = new Line2D.Double(xPoints[0], yPoints[0], xPoints[1], yPoints[1]);
            drawConditionalSequenceFlowIndicator(line, scaleFactor);
        }

        if (associationDirection == AssociationDirection.ONE || associationDirection == AssociationDirection.BOTH)
        {
            Line2D.Double line = new Line2D.Double(xPoints[xPoints.length - 2], yPoints[xPoints.length - 2],
                xPoints[xPoints.length - 1], yPoints[xPoints.length - 1]);
            drawArrowHead(line, scaleFactor);
        }
        if (associationDirection == AssociationDirection.BOTH)
        {
            Line2D.Double line = new Line2D.Double(xPoints[1], yPoints[1], xPoints[0], yPoints[0]);
            drawArrowHead(line, scaleFactor);
        }
        g.setPaint(originalPaint);
        g.setStroke(originalStroke);
    }

    /**
     * 高亮节点设置
     *
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public void drawHighLight(int x, int y, int width, int height)
    {
        Paint originalPaint = g.getPaint();
        Stroke originalStroke = g.getStroke();
        //设置高亮节点的颜色
        g.setPaint(HIGHLIGHT_COLOR);
        g.setStroke(THICK_TASK_BORDER_STROKE);
        RoundRectangle2D rect = new RoundRectangle2D.Double(x, y, width, height, 20, 20);
        g.draw(rect);
        g.setPaint(originalPaint);
        g.setStroke(originalStroke);
    }

    /**
     * 条件表达式 value字体设置
     *
     * @param text
     * @param graphicInfo
     * @param centered
     */
    public void drawLabel(String text, GraphicInfo graphicInfo, boolean centered)
    {
        float interline = 1.0f;
        if (text != null && text.length() > 0)
        {
            Paint originalPaint = g.getPaint();
            Font originalFont = g.getFont();
            g.setPaint(LABEL_COLOR);
            g.setFont(LABEL_FONT);

            int wrapWidth = 100;
            double textY = graphicInfo.getY();

            AttributedString as = new AttributedString(text);
            as.addAttribute(TextAttribute.FOREGROUND, g.getPaint());
            as.addAttribute(TextAttribute.FONT, g.getFont());
            AttributedCharacterIterator aci = as.getIterator();
            FontRenderContext frc = new FontRenderContext(null, true, false);
            LineBreakMeasurer lbm = new LineBreakMeasurer(aci, frc);

            while (lbm.getPosition() < text.length())
            {
                TextLayout tl = lbm.nextLayout(wrapWidth);
                textY += tl.getAscent();
                Rectangle2D bb = tl.getBounds();
                double tX = graphicInfo.getX();
                if (centered)
                {
                    tX += (int)(graphicInfo.getWidth() / 2 - bb.getWidth() / 2);
                }
                tl.draw(g, (float)tX, (float)textY);
                textY += tl.getDescent() + tl.getLeading() + (interline - 1.0f) * tl.getAscent();
            }
            // restore originals
            g.setFont(originalFont);
            g.setPaint(originalPaint);
        }
    }
}
