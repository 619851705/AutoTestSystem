package com.dcits.bean.message;
// default package



/**
 * ComplexParameter entity. @author MyEclipse Persistence Tools
 */

public class ComplexParameter{


    // Fields    

     private Integer id;
     private Parameter parameterByNextParameterId;
     private Parameter parameterBySelfParameterId;


    // Constructors

    /** default constructor */
    public ComplexParameter() {
    }

    
    /** full constructor */
    public ComplexParameter(Parameter parameterByNextParameterId, Parameter parameterBySelfParameterId) {
        this.parameterByNextParameterId = parameterByNextParameterId;
        this.parameterBySelfParameterId = parameterBySelfParameterId;
    }

   
    // Property accessors

    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public Parameter getParameterByNextParameterId() {
        return this.parameterByNextParameterId;
    }
    
    public void setParameterByNextParameterId(Parameter parameterByNextParameterId) {
        this.parameterByNextParameterId = parameterByNextParameterId;
    }

    public Parameter getParameterBySelfParameterId() {
        return this.parameterBySelfParameterId;
    }
    
    public void setParameterBySelfParameterId(Parameter parameterBySelfParameterId) {
        this.parameterBySelfParameterId = parameterBySelfParameterId;
    }
   








}