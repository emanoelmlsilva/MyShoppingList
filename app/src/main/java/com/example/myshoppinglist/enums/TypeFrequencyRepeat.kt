package com.example.myshoppinglist.enums

enum class TypeFrequencyRepeat(val type: Int, val title: String) {
     NEVER(-1, "nunca"), ALWAYS(0, "sempre"), ONE_MONTH(1, "1 - Mês"), TWO_MONTH(2, "2 - Mêses"), THREE_MONTH(3, "3 - Mêses"),
    FOUR_MONTH(4, "4 - Mêses"), FIVE_MONTH(5, "5 - Mêses"), SIX_MONTH(6, "6 - Mêses"), SEVEN_MONTH(7, "7 - Mêses"), EIGHT_MONTH(8, "8 - Mêses"),
    NINE_MONTH(9, "9 - Mêses"), TEN_MONTH(10, "10 - Mêses"), ELEVEN_MONTH(11, "11 - Mêses"), TWELVE_MONTH(12, "12 - Mêses");

    fun getMonth(): Int {
        return if(this.type == NEVER.type){ 0 } else { this.type + 1 }
    }
}