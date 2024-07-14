package bm.rest

import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test

class RestTest {
      
    @Test
    fun testIso() = runTest {
        val config = RestConfig("http://microg2.local/")
        
        println(config.putIso(Iso(800)))
        
        println(config.getIso())
        
        config.close()
    }
    
    @Test
    fun testWhiteBalance() = runTest {
        val config = RestConfig("http://microg2.local/")
        
        println(config.putWhiteBalance(WhiteBalance(2800)))
        println(config.getWhiteBalance())
        println(config.putWhiteBalanceDoAuto())
        println(config.getWhiteBalance())
        
        config.close()
    }
}
