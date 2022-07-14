package com.example.myshoppinglist.utils

import org.junit.Assert.*
import org.junit.Test

class MaskKiloGramTest{

    @Test
    fun maskKiloGramEmpty(){
        assertEquals("0.000", MaskUtils.maskKiloGram(""))
    }

    @Test
    fun maskKiloGramIncompleted(){
        assertEquals("0.008", MaskUtils.maskKiloGram("008"))
        assertEquals("0.008", MaskUtils.maskKiloGram("00.008"))
        assertEquals("1.345", MaskUtils.maskKiloGram("001.345"))
        assertEquals("0.135", MaskUtils.maskKiloGram("001.35"))
        assertEquals("0.007", MaskUtils.maskKiloGram("007."))
        assertEquals("1.008", MaskUtils.maskKiloGram("1008"))
        assertEquals("761.008", MaskUtils.maskKiloGram("76.10.08"))
        assertEquals("0.003", MaskUtils.maskKiloGram("3"))
        assertEquals("0.500", MaskUtils.maskKiloGram("500"))
    }
}