package com.learnwithme.buildapps.giantbomb;

import com.learnwithme.buildapps.giantbomb.data.model.GamePlatformInfoList;
import com.learnwithme.buildapps.giantbomb.utils.ClassUtils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ClassUtilsTest {
    @Test
    public void testGetFieldsReturnsCorrectString_forAutoValueClass() {

        // Arrange
        String detectedAVFields = ClassUtils.getMethodsList(GamePlatformInfoList.class);
        String actualFields = "company,id,image,name,original_price,release_date";

        // Assert
        assertEquals("getMethodsList method returned incorrect list!",
                actualFields,
                detectedAVFields);
    }
}