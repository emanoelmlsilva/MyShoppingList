package com.example.myshoppinglist.utils

import org.junit.Assert.*
import org.junit.Test

class MaskUtilsTest{

    @Test
    fun mask_value_empty(){
        assertEquals("0,00", MaskUtils.maskValue(""))
    }

    @Test
    fun mask_value_character_special(){
        assertEquals("1,32", MaskUtils.maskValue("1,32."))
    }

    @Test
    fun mask_value_decimal(){
        assertEquals("25,80", MaskUtils.maskValue("25,8"))
//        assertEquals("02,00", MaskUtils.maskValue("2,0"))
//        assertEquals("0,20", MaskUtils.maskValue("0,2"))
//        assertEquals("0,02", MaskUtils.maskValue("0,002"))
//        assertEquals("0,20", MaskUtils.maskValue("0,020"))
//        assertEquals("2,00", MaskUtils.maskValue("0,200"))
//        assertEquals("1,20", MaskUtils.maskValue("1,2"))
//        assertEquals("12,00", MaskUtils.maskValue("1,200"))
//        assertEquals("12,20", MaskUtils.maskValue("1,220"))
//        assertEquals("10,23", MaskUtils.maskValue("1,023"))
    }

    @Test
    fun mask_value_hundreds(){
        assertEquals("252,20", MaskUtils.maskValue("25,220"))
        assertEquals("25,22", MaskUtils.maskValue("0252,2"))
        assertEquals("10,25", MaskUtils.maskValue("102,5"))
        assertEquals("25,03", MaskUtils.maskValue(".025,03"))
        assertEquals("325,03", MaskUtils.maskValue(".325,03"))
    }

    @Test
    fun mask_value_thousands(){
        assertEquals("2.522,09", MaskUtils.maskValue("252,209"))
        assertEquals("1.025,22", MaskUtils.maskValue("10252,2"))
        assertEquals("10.252,20", MaskUtils.maskValue("10252,20"))
        assertEquals("9.025,03", MaskUtils.maskValue("902,503"))
        assertEquals("99.999,99", MaskUtils.maskValue("99999.99"))
    }
}