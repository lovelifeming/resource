package com.zsm.flowable.service;

import org.flowable.bpmn.model.*;
import org.flowable.image.impl.DefaultProcessDiagramGenerator;

import java.util.List;


/**
 * @Author: zeng.
 * @Date:Created in 2021-02-02 17:30.
 * @Description:
 */
public class MyProcessDiagramGenerator extends DefaultProcessDiagramGenerator
{
    protected MyProcessDiagramCanvas generateProcessDiagram(BpmnModel bpmnModel, String imageType,
                                                            List<String> highLightedActivities,
                                                            List<String> highLightedFlows,
                                                            String activityFontName, String labelFontName,
                                                            String annotationFontName, ClassLoader customClassLoader,
                                                            double scaleFactor, boolean drawSequenceFlowNameWithNoLabelDI)
    {
        this.prepareBpmnModel(bpmnModel);
        MyProcessDiagramCanvas processDiagramCanvas = initProcessDiagramCanvas(bpmnModel, imageType,
            activityFontName, labelFontName, annotationFontName, customClassLoader);
        // 实现同父类实现一模一样

        // Draw pool shape, if process is participant in collaboration
        for (Pool pool : bpmnModel.getPools())
        {
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(pool.getId());
            processDiagramCanvas.drawPoolOrLane(pool.getName(), graphicInfo, scaleFactor);
        }

        // Draw lanes
        for (org.flowable.bpmn.model.Process process : bpmnModel.getProcesses())
        {
            for (Lane lane : process.getLanes())
            {
                GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(lane.getId());
                processDiagramCanvas.drawPoolOrLane(lane.getName(), graphicInfo, scaleFactor);
            }
        }

        // Draw activities and their sequence-flows
        for (org.flowable.bpmn.model.Process process : bpmnModel.getProcesses())
        {
            for (FlowNode flowNode : process.findFlowElementsOfType(FlowNode.class))
            {
                if (!isPartOfCollapsedSubProcess(flowNode, bpmnModel))
                {
                    drawActivity(processDiagramCanvas, bpmnModel, flowNode, highLightedActivities, highLightedFlows,
                        scaleFactor);
                }
            }
        }

        // Draw artifacts
        for (org.flowable.bpmn.model.Process process : bpmnModel.getProcesses())
        {

            for (Artifact artifact : process.getArtifacts())
            {
                drawArtifact(processDiagramCanvas, bpmnModel, artifact);
            }

            List<SubProcess> subProcesses = process.findFlowElementsOfType(SubProcess.class, true);
            if (subProcesses != null)
            {
                for (SubProcess subProcess : subProcesses)
                {

                    GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(subProcess.getId());
                    if (graphicInfo != null && graphicInfo.getExpanded() != null && !graphicInfo.getExpanded())
                    {
                        continue;
                    }

                    if (!isPartOfCollapsedSubProcess(subProcess, bpmnModel))
                    {
                        for (Artifact subProcessArtifact : subProcess.getArtifacts())
                        {
                            drawArtifact(processDiagramCanvas, bpmnModel, subProcessArtifact);
                        }
                    }
                }
            }
        }
        return processDiagramCanvas;
    }

    protected static MyProcessDiagramCanvas initProcessDiagramCanvas(BpmnModel bpmnModel, String imageType,
                                                                     String activityFontName, String labelFontName,
                                                                     String annotationFontName,
                                                                     ClassLoader customClassLoader)
    {
        // 这里与父类代码一模一样
        // We need to calculate maximum values to know how big the image will be in its entirety
        double minX = Double.MAX_VALUE;
        double maxX = 0;
        double minY = Double.MAX_VALUE;
        double maxY = 0;

        for (Pool pool : bpmnModel.getPools())
        {
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(pool.getId());
            minX = graphicInfo.getX();
            maxX = graphicInfo.getX() + graphicInfo.getWidth();
            minY = graphicInfo.getY();
            maxY = graphicInfo.getY() + graphicInfo.getHeight();
        }

        List<FlowNode> flowNodes = gatherAllFlowNodes(bpmnModel);
        for (FlowNode flowNode : flowNodes)
        {

            GraphicInfo flowNodeGraphicInfo = bpmnModel.getGraphicInfo(flowNode.getId());

            // width
            if (flowNodeGraphicInfo.getX() + flowNodeGraphicInfo.getWidth() > maxX)
            {
                maxX = flowNodeGraphicInfo.getX() + flowNodeGraphicInfo.getWidth();
            }
            if (flowNodeGraphicInfo.getX() < minX)
            {
                minX = flowNodeGraphicInfo.getX();
            }
            // height
            if (flowNodeGraphicInfo.getY() + flowNodeGraphicInfo.getHeight() > maxY)
            {
                maxY = flowNodeGraphicInfo.getY() + flowNodeGraphicInfo.getHeight();
            }
            if (flowNodeGraphicInfo.getY() < minY)
            {
                minY = flowNodeGraphicInfo.getY();
            }

            for (SequenceFlow sequenceFlow : flowNode.getOutgoingFlows())
            {
                List<GraphicInfo> graphicInfoList = bpmnModel.getFlowLocationGraphicInfo(sequenceFlow.getId());
                if (graphicInfoList != null)
                {
                    for (GraphicInfo graphicInfo : graphicInfoList)
                    {
                        // width
                        if (graphicInfo.getX() > maxX)
                        {
                            maxX = graphicInfo.getX();
                        }
                        if (graphicInfo.getX() < minX)
                        {
                            minX = graphicInfo.getX();
                        }
                        // height
                        if (graphicInfo.getY() > maxY)
                        {
                            maxY = graphicInfo.getY();
                        }
                        if (graphicInfo.getY() < minY)
                        {
                            minY = graphicInfo.getY();
                        }
                    }
                }
            }
        }

        List<Artifact> artifacts = gatherAllArtifacts(bpmnModel);
        for (Artifact artifact : artifacts)
        {

            GraphicInfo artifactGraphicInfo = bpmnModel.getGraphicInfo(artifact.getId());

            if (artifactGraphicInfo != null)
            {
                // width
                if (artifactGraphicInfo.getX() + artifactGraphicInfo.getWidth() > maxX)
                {
                    maxX = artifactGraphicInfo.getX() + artifactGraphicInfo.getWidth();
                }
                if (artifactGraphicInfo.getX() < minX)
                {
                    minX = artifactGraphicInfo.getX();
                }
                // height
                if (artifactGraphicInfo.getY() + artifactGraphicInfo.getHeight() > maxY)
                {
                    maxY = artifactGraphicInfo.getY() + artifactGraphicInfo.getHeight();
                }
                if (artifactGraphicInfo.getY() < minY)
                {
                    minY = artifactGraphicInfo.getY();
                }
            }

            List<GraphicInfo> graphicInfoList = bpmnModel.getFlowLocationGraphicInfo(artifact.getId());
            if (graphicInfoList != null)
            {
                for (GraphicInfo graphicInfo : graphicInfoList)
                {
                    // width
                    if (graphicInfo.getX() > maxX)
                    {
                        maxX = graphicInfo.getX();
                    }
                    if (graphicInfo.getX() < minX)
                    {
                        minX = graphicInfo.getX();
                    }
                    // height
                    if (graphicInfo.getY() > maxY)
                    {
                        maxY = graphicInfo.getY();
                    }
                    if (graphicInfo.getY() < minY)
                    {
                        minY = graphicInfo.getY();
                    }
                }
            }
        }

        int nrOfLanes = 0;
        for (org.flowable.bpmn.model.Process process : bpmnModel.getProcesses())
        {
            for (Lane l : process.getLanes())
            {

                nrOfLanes++;

                GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(l.getId());
                // // width
                if (graphicInfo.getX() + graphicInfo.getWidth() > maxX)
                {
                    maxX = graphicInfo.getX() + graphicInfo.getWidth();
                }
                if (graphicInfo.getX() < minX)
                {
                    minX = graphicInfo.getX();
                }
                // height
                if (graphicInfo.getY() + graphicInfo.getHeight() > maxY)
                {
                    maxY = graphicInfo.getY() + graphicInfo.getHeight();
                }
                if (graphicInfo.getY() < minY)
                {
                    minY = graphicInfo.getY();
                }
            }
        }

        // Special case, see https://activiti.atlassian.net/browse/ACT-1431
        if (flowNodes.isEmpty() && bpmnModel.getPools().isEmpty() && nrOfLanes == 0)
        {
            // Nothing to show
            minX = 0;
            minY = 0;
        }
        //设置返回自定义ProcessDiagramCanvas
        return new MyProcessDiagramCanvas((int)maxX + 10, (int)maxY + 10, (int)minX, (int)minY, imageType,
            activityFontName, labelFontName, annotationFontName, customClassLoader);
    }
}
