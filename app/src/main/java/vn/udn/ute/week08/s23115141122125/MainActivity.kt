
package vn.udn.ute.week08.s23115141122125

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etQt: EditText
    private lateinit var etCk: EditText
    private lateinit var tvResult: TextView

    private val scorePattern = Regex("""\d+(\.\d+)?""")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etName = findViewById(R.id.etName)
        etQt = findViewById(R.id.etQt)
        etCk = findViewById(R.id.etCk)
        tvResult = findViewById(R.id.tvResult)

        val btnCheck = findViewById<Button>(R.id.btnCheck)
        val btnClear = findViewById<Button>(R.id.btnClear)

        btnCheck.setOnClickListener {
            checkCourse()
        }

        btnClear.setOnClickListener {
            clearForm()
        }
    }

    // Helper 1: Kiểm tra tên học phần
    private fun validateName(): String? {
        val name = etName.text.toString().trim()

        if (name.length !in 1..40) {
            etName.error = "Tên phải có từ 1 đến 40 ký tự"
            return null
        }

        return name
    }

    // Helper 2: Kiểm tra điểm
    private fun validateScore(
        field: EditText,
        label: String
    ): Double? {
        val input = field.text.toString().trim()

        val score = if (scorePattern.matches(input)) {
            input.toDoubleOrNull()
        } else {
            null
        }

        if (score == null ||
            !score.isFinite() ||
            score < 0.0 ||
            score > 10.0
        ) {
            field.error = "$label phải từ 0 đến 10, dùng dấu chấm"
            return null
        }

        return score
    }

    // Helper 3: Tính điểm tổng kết
    private fun calculateTotal(
        qt: Double,
        ck: Double
    ): Double {
        return 0.4 * qt + 0.6 * ck
    }

    // Helper 4: Phân loại
    private fun isPassed(
        total: Double,
        ck: Double
    ): Boolean {
        return total >= 5.5 && ck >= 3.0
    }

    private fun checkCourse() {
        // Không giữ kết quả cũ khi dữ liệu mới bị lỗi
        tvResult.text = ""
        clearErrors()

        val name = validateName()
        val qt = validateScore(etQt, "QT")
        val ck = validateScore(etCk, "CK")

        // Chỉ tính khi cả ba trường hợp lệ
        if (name == null || qt == null || ck == null) {
            return
        }

        val total = calculateTotal(qt, ck)

        val conclusion = if (isPassed(total, ck)) {
            "Đạt"
        } else {
            "Chưa đạt"
        }

        val scoreText = String.format(
            Locale.US,
            "QT: %.2f\nCK: %.2f\nT: %.2f",
            qt, ck, total
        )

        tvResult.text =
            "Tên học phần: $name\n" +
                    scoreText + "\n" +
                    "Kết luận: $conclusion"
    }

    private fun clearErrors() {
        etName.error = null
        etQt.error = null
        etCk.error = null
    }

    private fun clearForm() {
        etName.text.clear()
        etQt.text.clear()
        etCk.text.clear()
        tvResult.text = ""
        clearErrors()
    }
}
