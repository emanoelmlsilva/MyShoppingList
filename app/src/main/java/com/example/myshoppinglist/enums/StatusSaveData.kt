package com.example.myshoppinglist.enums

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myshoppinglist.R

enum class StatusSaveData(val icon: Int, val message: String, val width: Dp) {
    WAITING(R.raw.waiting, "Tentando comunicação com o servidor.", 90.dp),
    SAVE(R.raw.save_internal, "Dados salvos internamente.", 20.dp),
    DELETE(R.raw.delete, "Dados deletados internamente", 45.dp),
    SEND(R.raw.send, "Enviando dados para o servidor.", 30.dp),
    UPDATE(R.raw.update, "Dados atualizado internamente", 20.dp),
    TRANSFER(R.raw.transfer, "Dados transferido internamente", 40.dp),
    ERROR(R.raw.error, "Não foi possível conectar.", 20.dp),
    ERROR_DELETE(-0, "Não foi possível conectar", 20.dp);
}