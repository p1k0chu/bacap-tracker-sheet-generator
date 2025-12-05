package io.github.p1k0chu.bacaptrackersheetgenerator

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.theme
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.path
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.nio.file.Path
import kotlin.io.path.outputStream

class Main : CliktCommand("bacap-tracker-sheet-generator") {
    private val datapack: Path by option("-p", "--pack", help = "datapack's root folder (not a zip file!)").path(
        mustExist = true,
        canBeFile = false,
        mustBeReadable = true
    ).required()

    private val docs: Path by option("-d", "--docs", help = "the documentation sheet (xlsx file)").path(
        mustExist = true,
        canBeDir = false,
        mustBeReadable = true
    ).required()

    private val iconsVersion: String by option(
        "--icons-version",
        help = "minecraft version (only for icons from nerothe)\u0085default: 1.21.8"
    ).default("1.21.8")

    private val transPath: Path by option("-t", "--trans", help = "the path to translation file extracted from client.jar (for item names)").path(
        mustExist = true,
        canBeDir = false,
        mustBeReadable = true
    ).required()

    private val output: Path by option("-o", "--output", help = "output sheet (xlsx)")
        .path(canBeDir = false)
        .default(Path.of("./out.xlsx"))

    override fun helpEpilog(context: Context): String {
        return context.theme.info("Example:\u0085$commandName -p ./bac -d ./doc.xlsx -t ./en_us.json -o my_sheet.xlsx")
    }

    override fun run() {
        val docsWorkbook: Workbook = WorkbookFactory.create(docs.toFile())
        val workbook = XSSFWorkbook()

        workbook.generateAdvancementsTab(docsWorkbook, datapack, iconsVersion)
        workbook.generateItemsTab(datapack, iconsVersion, transPath)

        workbook.write(output.outputStream())
        workbook.close()
    }
}

fun main(args: Array<String>) = Main().main(args)
