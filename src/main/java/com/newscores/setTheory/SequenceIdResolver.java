package com.newscores.setTheory;

import com.newscores.setTheory.interfaces.*;
import com.newscores.setTheory.*;
import com.fasterxml.jackson.databind.jsontype.impl.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.annotation.*;


public class SequenceIdResolver extends TypeIdResolverBase {
    
    private JavaType superType;
 
    @Override
    public void init(JavaType baseType) {
        superType = baseType;
    }
 
    @Override
    public JsonTypeInfo.Id getMechanism() {
        return JsonTypeInfo.Id.NAME;
    }
 
    @Override
    public String idFromValue(Object obj) {
        return idFromValueAndType(obj, obj.getClass());
    }
 
    @Override
    public String idFromValueAndType(Object obj, Class<?> subType) {
        String typeId = null;
        switch (subType.getSimpleName()) {
        case "PitchClassSequence":
            typeId = "pitch_class_sequence";
            break;
        case "PitchSequence":
            typeId = "pitch_sequence";
            break;
        case "Row":
        	typeId = "row";
        	break;
        case "Contour":
            typeId = "contour";
    }
        
        return typeId;
    }
 
    @Override
    public JavaType typeFromId(DatabindContext context, String id) {
        Class<?> subType = null;
        switch (id) {
        case "pitch_class_sequence":
            subType = PitchClassSequence.class;
            break;
        case "pitch_sequence":
            subType = PitchSequence.class;
            break;
    	case "row":
    		subType = Row.class;
    		break;
    	case "contour":
    		subType = Contour.class;
    	}
        return context.constructSpecializedType(superType, subType);
    }
}