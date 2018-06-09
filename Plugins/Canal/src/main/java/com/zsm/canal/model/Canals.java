package com.zsm.canal.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/5/18.
 * @Modified By:
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "canals")
public class Canals
{
    private String pattern;

    @XmlElement(name = "cluster")
    List<Cluster> clusters;

    @XmlElement(name = "simple")
    List<Simple> simples;

    @XmlElement(name = "emails")
    Email emails;

    public String getPattern()
    {
        return pattern;
    }

    public void setPattern(String pattern)
    {
        this.pattern = pattern;
    }

    public List<Cluster> getClusters()
    {
        return clusters;
    }

    public void setClusters(List<Cluster> clusters)
    {
        this.clusters = clusters;
    }

    public List<Simple> getSimples()
    {
        return simples;
    }

    public void setSimples(List<Simple> simples)
    {
        this.simples = simples;
    }

    public Email getEmails()
    {
        return emails;
    }

    public void setEmails(Email emails)
    {
        this.emails = emails;
    }

    @Override
    public String toString()
    {
        return "Canals{" +
               "pattern='" + pattern + '\'' +
               ", clusters=" + clusters +
               ", simples=" + simples +
               ", emails=" + emails +
               '}';
    }
}
