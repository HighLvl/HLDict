package com.example.onscreendictionary.ui.data

import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.view.View
import androidx.core.text.HtmlCompat
import com.example.onscreendictionary.domain.data.WordDefinition
import com.example.onscreendictionary.domain.data.WordDefinitionId
import com.example.onscreendictionary.ui.view.RomanNumeral
import java.util.regex.Pattern

data class FormattedWordDefinition(
    val id: WordDefinitionId,
    val title: CharSequence,
    val isFull: Boolean,
    val isFavorite: Boolean,
    val num: CharSequence?,
    val pos: CharSequence,
    val gloss: CharSequence,
    val syllables: CharSequence,
    val lang: CharSequence,
    val examplesText: CharSequence,
    val examplesVisibility: Int,
    val synonyms: CharSequence,
    val synonymsVisibility: Int,
    val antonyms: CharSequence,
    val antonymsVisibility: Int,
    val hyponyms: CharSequence?,
    val hyponymsVisibility: Int,
    val hypernyms: CharSequence?,
    val hypernymsVisibility: Int,
    val etymology: CharSequence?,
    val etymologyVisibility: Int,
    val phras: CharSequence?,
    val phrasVisibility: Int,
    val ipa: CharSequence?,
    val ipaVisibility: Int
)

fun WordDefinition.toFormatted(
    useAbbr: Boolean = true
): FormattedWordDefinition {
    val senseNum = id.sense?.plus(1)?.let(RomanNumeral::value)?.plus(".")
    val glossNum = id.gloss?.plus(1)?.toString()?.plus(".")

    return FormattedWordDefinition(
        id = id,
        title = title,
        isFull = isFull,
        isFavorite = isFavorite,
        num = listOfNotNull(senseNum, glossNum).joinToString("").ifEmpty { null },
        pos = pos,
        gloss = formatValue(gloss, useAbbr),
        syllables = syllables,
        lang = lang,
        examplesText = formatExamples(examples, useAbbr),
        examplesVisibility = selectGoneIfEmpty(examples),
        synonyms = formatValue(synonyms, useAbbr),
        synonymsVisibility = selectGoneIfEmpty(synonyms),
        antonyms = formatValue(antonyms, useAbbr),
        antonymsVisibility = selectGoneIfEmpty(antonyms),
        hyponyms = hyponyms?.let { formatValue(it, useAbbr) },
        hypernymsVisibility = selectGoneIfEmpty(hyponyms),
        hypernyms = hypernyms?.let { formatValue(it, useAbbr) },
        hyponymsVisibility = selectGoneIfEmpty(hypernyms),
        etymology = etymology?.let { formatValue(it, useAbbr) },
        etymologyVisibility = selectGoneIfEmpty(etymology),
        phras = phras?.filter { it.isNotBlank() }?.joinToString("\n") { "\"${formatValue(it, useAbbr)}\"" },
        phrasVisibility = selectGoneIfEmpty(phras),
        ipa = ipa?.let { formatValue(it, useAbbr) },
        ipaVisibility = selectGoneIfEmpty(ipa)
    )
}

private fun formatValue(value: String, useAbbr: Boolean): CharSequence {
    return fromHtml(
        value.replace("""\{a full_text=[^\}]+\}""".toRegex(), "")
            .replace("{/a}", "")
    )
}

private fun formatExamples(examples: List<String>, useAbbr: Boolean): Spanned {
    var index = -1
    val examplesHtml = examples.joinToString("") {
        index++
        if (index < examples.lastIndex)
            "<p><i><b></b>$it<b></b></i><br><br></p>"
        else
            "<i><b></b>$it<b></b></i>"
    }
        .replace("{wtitle}", "<b>")
        .replace("{/wtitle}", "</b>")

    val examplesSpanned = fromHtml(examplesHtml)
    val spannableString = SpannableString(examplesSpanned)
    val matcher = Pattern.compile("\n\n").matcher(examplesSpanned)
    while (matcher.find()) {
        spannableString.setSpan(
            AbsoluteSizeSpan(
                LINE_SPACING_BETWEEN_EXAMPLES,
                true
            ),
            matcher.start() + 1,
            matcher.end(),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    return spannableString
}

private const val LINE_SPACING_BETWEEN_EXAMPLES = 4

private fun selectGoneIfEmpty(list: List<String>?) =
    if (list.isNullOrEmpty() || list.all { it.isEmpty() }) View.GONE else View.VISIBLE

private fun selectGoneIfEmpty(string: String?) =
    if (string.isNullOrEmpty()) View.GONE else View.VISIBLE

private fun fromHtml(text: String) =
    HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH)
