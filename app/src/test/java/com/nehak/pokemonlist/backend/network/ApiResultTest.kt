package com.nehak.pokemonlist.backend.network

import com.nehak.pokemonlist.backend.other.ApiResult
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Created by Neha Kushwah on 8/9/21.
 */
@RunWith(JUnit4::class)
class ApiResultTest {

    @Test
    fun exception() {
        val apiResponse = ApiResult.error<String>("error");
        MatcherAssert.assertThat(apiResponse.message, CoreMatchers.`is`("error"))
    }

    @Test
    fun success() {
        val apiResponse = ApiResult.success("data")
        if (apiResponse.status == ApiResult.Status.SUCCESS) {
            MatcherAssert.assertThat(apiResponse.data, CoreMatchers.`is`("data"))
        }
    }
}
