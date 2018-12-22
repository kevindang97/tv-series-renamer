# TV Series Renamer
A small Java application made for the purpose of renaming multiple files for a TV series or similar format into a standard filename format.

## Current Features
- Currently only support for a single format: [series-name] - s[season-number]e[episode-number] - [episode-name] (example: Steven Universe - s01e03 - Cheeseburger Backpack)
- Preview the before and after filenames
- Input episode names en masse
- Edit individual episode names
- Reorder the input files in case some episodes are out of order

## Todos
- Support for custom formats
- Add some CSS to make it look more pretty and less like your generic Java application
- Maybe using the parent folder names to perform some automatic detection of series name and season number
- Maybe use TVDB API to automaticall retrieve episode names 

## Current Issues
- Does not work properly when the before and after filenames overlap each other (This issue comes up when this tool is used to only reorder episodes and not rename them. A temporary workaround is to change something like the season number in order to perform the reorder and then change it back to the original season number afterwards.)
