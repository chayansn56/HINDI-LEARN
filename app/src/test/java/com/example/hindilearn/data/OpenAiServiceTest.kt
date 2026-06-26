package com.example.hindilearn.data

import org.junit.Assert.assertNotNull
import org.junit.Test

class OpenAiServiceTest {
    @Test
    fun testOpenAiServiceInitialization() {
        // Verify OpenAiService singleton is not null and loads properly
        assertNotNull(OpenAiService)
    }
}
