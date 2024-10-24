/*
 * Copyright 2019-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.vividus.condition;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Properties;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

class AbstractBooleanPropertyConditionTests
{
    private static final String TRUE = "true";
    private static final String FALSE = "false";
    private static final String FIRST_PROP = "prop1";
    private static final String SECOND_PROP = "prop2";

    static Stream<Arguments> propertyValues()
    {
        return Stream.of(
                arguments(TRUE, TRUE, true),
                arguments(FALSE, FALSE, false),
                arguments(FALSE, TRUE, false),
                arguments(TRUE, FALSE, false),
                arguments(null, TRUE, false),
                arguments(EMPTY, TRUE, false)
        );
    }

    @ParameterizedTest
    @MethodSource("propertyValues")
    void testCondition(String firstPropValue, String secondPropValue, Boolean expected)
    {
        var beanFactory = mock(ConfigurableListableBeanFactory.class);
        var metadata = mock(AnnotatedTypeMetadata.class);
        var context = mock(ConditionContext.class);
        var properties = mock(Properties.class);

        when(context.getBeanFactory()).thenReturn(beanFactory);
        when(beanFactory.getBean("properties", Properties.class)).thenReturn(properties);
        when(properties.getProperty(FIRST_PROP)).thenReturn(firstPropValue);
        when(properties.getProperty(SECOND_PROP)).thenReturn(secondPropValue);

        var condition = new BooleanPropertyCondition();
        Assertions.assertEquals(expected, condition.matches(context, metadata));
        verifyNoInteractions(metadata);
    }

    static class BooleanPropertyCondition extends AbstractBooleanPropertyCondition
    {
        BooleanPropertyCondition()
        {
            super(FIRST_PROP, SECOND_PROP);
        }
    }
}
