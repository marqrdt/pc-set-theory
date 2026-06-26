package org.paulmarquardt.setTheory;

import org.paulmarquardt.setTheory.interfaces.*;
import org.paulmarquardt.setTheory.*;
import com.fasterxml.jackson.databind.jsontype.impl.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.annotation.*;

public class SetIdResolver extends TypeIdResolverBase {
    
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
        case "PitchClassSet":
            typeId = "pitch_class_set";
            break;
        case "PitchSet":
            typeId = "pitch_set";
    }
        
        return typeId;
    }
 
    @Override
    public JavaType typeFromId(DatabindContext context, String id) {
        Class<?> subType = null;
        switch (id) {
        case "pitch_class_set":
            subType = PitchClassSet.class;
            break;
        case "pitch_set":
            subType = PitchSet.class;
    	}
        return context.constructSpecializedType(superType, subType);
    }
}