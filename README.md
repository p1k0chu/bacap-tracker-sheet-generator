# BACAP Tracker sheet generator

automatically generates a list of advancements and items/blocks required for the tracker to work

# Usage:

You can (soon TM) download the generated sheets instead of running the program. If you do, skip to step 10

To run this program you need java installed.

1. Download latest release and unzip
2. Download bac datapack
3. Download bac documentation spreadsheet for its version
4. Go to `minecraft.wiki` and pick your mc version, download `client.jar`...
    1. rename `client.jar` to `client.zip`, unzip
    2. go to `client/assets/minecraft/lang`, find `en_us.json`
5. copy datapack, docs and translation file into generator's bin folder for convenience
6. you can rename the files to something short like `bac.zip`, `doc.xlsx`
7. unzip the datapack!
8. open that `bin` folder in terminal
9. you can see how to use the program by running `./bacap-tracker-sheet-generator -h` (.bat file for windows)  
    example: `./bacap-tracker-sheet-generator -p ./bac -d ./doc.xlsx -t ./en_us.json -o tracker.xlsx`
10. now you have a spreadsheet with auto generated list of advancements and items, which work with bacap tracker
    - you can upload it to your google drive, and then you can copy both of the sheets into your existing spreadsheet (right click tabs on the bottom and copy into existing spreadsheet)
    - you can move columns to match your tracker's columns order (if its different, you **have to** do it)
    - dont forget to rename the sheets to match your tracker too (e.g. "Items-Blocks" -> "Items/Blocks" or whatever your tracker uses)

# More things

This thing generates everything automatically so its simply impossible to assign a class to the advancements (e.g. 'Unique' or 'Subset').

Wont keep any custom notes you already have for the advancements.

But! you can combine multiple datapacks into one and generate a combined list for all advancements.  
this means if you play with like 5 addons you can generate a tracker for everything  
theres no way rn to use multiple documentations tho (unless you manually merge the spreadsheets but, like, why)

you can choose mc version used for icons from nerothe (see step 9).  
so if you play mc 1.21.6 you'd do `--icons-version 1.21.6`, if you play 1.21.8, you'd do `--icons-version 1.21.8` (optional, 1.21.8 is the default)
