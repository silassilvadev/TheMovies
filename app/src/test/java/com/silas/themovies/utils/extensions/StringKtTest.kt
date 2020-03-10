package com.silas.themovies.utils.extensions

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class StringKtTest {

    @Test
    fun `Convert in date string invalid characters error`() {
        val listCases =
            arrayListOf("13-12-2019", "13*12*2019", "13_12_2019", "13.12.2019", "13,12,2019", "13=12=2019",
                "13!12!2019", "13?12?2019", "13?12?2019", "13'12'2019", "13a12a2019",
                "32/01/2017", "29/02/2019", "30/02/2017", "15/13/2019", "14/0/2018",
                "18/07/2021")

        var isSuccess = false

        for (item in listCases) {
            if (item.formatDate().isNotBlank()) {
                isSuccess = true
                break
            }
        }

        assertFalse("Date is converted failed", isSuccess)
    }

    @Test
    fun `Convert in date string success`() {
        val listCases = arrayListOf("13/12/2019", "20/01/2020", "05/04/1996", "15/08/1974", "13/10/1978")

        var isSuccess = false

        for (item in listCases) {
            if (item.formatDate().isNotBlank()) {
                isSuccess = true
                break
            }
        }

        assertTrue("Date is converted success", isSuccess)
    }
}
