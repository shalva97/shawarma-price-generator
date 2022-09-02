import androidx.compose.runtime.*
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposable

fun main() {
    var ingredientList by mutableStateOf(ShawarmaState.empty())

    renderComposable(rootElementId = "root") {
        ingredientList.ingredients.value.forEach { ingredientEntry ->
            Div({ style { padding(25.px) } }) {
                Button(attrs = {
                    onClick { ingredientList = ingredientList.decrement(ingredientEntry.key) }
                }) {
                    Text("-")
                }

                Span({ style { padding(15.px) } }) {
                    Text("${ingredientEntry.value}")
                }

                Button(attrs = {
                    onClick {
                        ingredientList = ingredientList.increment(ingredientEntry.key)
                    }
                }) {
                    Text("+")
                }
                Text(" " + ingredientEntry.key.title)
            }
        }

        Text("Total price: " + ingredientList.sum + " GEL")

    }
}

data class ShawarmaState(
    val ingredients: State<Map<ShawarmaIngredient, Int>>,
) {
    val sum
        get() = ingredients.value.entries.sumOf {
            it.value.toDouble() * it.key.pricePerKG.toDouble() / 100f / 1000f
        }.toString().take(4)

    fun decrement(ingredient: ShawarmaIngredient): ShawarmaState {
        val data = ingredients.value.toMutableMap()
        data[ingredient] = data[ingredient]?.dec() ?: 0
        return copy(ingredients = mutableStateOf(data))
    }

    fun increment(ingredient: ShawarmaIngredient): ShawarmaState {
        val data = ingredients.value.toMutableMap()
        data[ingredient] = data[ingredient]?.inc() ?: 0
        return copy(ingredients = mutableStateOf(data))
    }

    companion object {
        fun empty(): ShawarmaState {
            return ShawarmaState(
                mutableStateOf(ShawarmaIngredient.values().associateWith { 0 })
            )
        }
    }
}