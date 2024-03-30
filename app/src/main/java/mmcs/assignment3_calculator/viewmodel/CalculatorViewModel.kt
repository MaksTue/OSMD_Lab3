package mmcs.assignment3_calculator.viewmodel

import androidx.databinding.BaseObservable
import androidx.databinding.ObservableField

class CalculatorViewModel: BaseObservable(), Calculator {
    private var currentInput: StringBuilder = StringBuilder()
    private var currentOperation: Operation? = null
    private var operand: Double = 0.0
    private var displayValue: Double = 0.0

    override var display: ObservableField<String> = ObservableField("0")

    override fun addDigit(dig: Int) {
        currentInput.append(dig)
        display.set(currentInput.toString())
    }

    override fun addPoint() {
        if (!currentInput.contains('.')) {
            currentInput.append('.')
            display.set(currentInput.toString())
        }
    }

    override fun addOperation(op: Operation) {
        val inputValue = currentInput.toString().toDoubleOrNull()
        if (inputValue != null) {
            if (currentOperation != null) {
                compute()
            }
            currentOperation = op
            operand = inputValue
            currentInput.clear()

        } else if (currentOperation != null) {
            // Заменяем предыдущий оператор на новый
            currentOperation = op
        }
    }

    override fun compute() {
        val secondOperand = currentInput.toString().toDouble()
        when (currentOperation) {
            Operation.ADD -> displayValue = operand + secondOperand
            Operation.SUB -> displayValue = operand - secondOperand
            Operation.MUL -> displayValue = operand * secondOperand
            Operation.DIV -> {
                if (secondOperand != 0.0) {
                    displayValue = operand / secondOperand
                } else {
                    displayValue = Double.NaN // Division by zero
                }
            }
            else -> return
        }
        currentInput = StringBuilder(displayValue.toString())
        currentOperation = null
        updateDisplay()
    }

    override fun clear() {
        if (currentInput.isNotEmpty()) {
            currentInput.deleteCharAt(currentInput.length - 1)
            display.set(currentInput.toString())
        }
    }

    override fun reset() {
        currentInput = StringBuilder()
        currentOperation = null
        operand = 0.0
        displayValue = 0.0
        display.set("0")
    }

    private fun updateDisplay() {
        val value = if (currentInput.isEmpty()) {
            "0"
        } else {
            currentInput.toString()
        }
        display.set(value)
    }
}