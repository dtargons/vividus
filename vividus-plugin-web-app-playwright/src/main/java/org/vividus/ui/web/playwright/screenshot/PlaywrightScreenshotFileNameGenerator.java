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

package org.vividus.ui.web.playwright.screenshot;

import java.time.Instant;

import com.microsoft.playwright.options.ViewportSize;

import org.vividus.ui.screenshot.ScreenshotFileNameGenerator;
import org.vividus.ui.web.playwright.BrowserContextProvider;
import org.vividus.ui.web.playwright.UiContext;

public class PlaywrightScreenshotFileNameGenerator implements ScreenshotFileNameGenerator
{
    private final BrowserContextProvider browserContextProvider;
    private final UiContext uiContext;

    public PlaywrightScreenshotFileNameGenerator(BrowserContextProvider browserContextProvider, UiContext uiContext)
    {
        this.browserContextProvider = browserContextProvider;
        this.uiContext = uiContext;
    }

    @Override
    public String generateScreenshotFileName(String screenshotName)
    {
        ViewportSize viewportSize = uiContext.getCurrentPage().viewportSize();
        String browserName = browserContextProvider.get().browser().browserType().name();
        return String.format("%s-%s-%s-%dx%d.%s", DATE_FORMAT.format(Instant.now()), screenshotName, browserName,
                viewportSize.width, viewportSize.height, DEFAULT_IMAGE_FORMAT);
    }
}
