# TV Series Renamer
A small Java application that will rename a set of files in a TV series into a standardised format

The following is just my ramblings and my thoughts on this project. I'll keep them here because this project is only in its infancy so why not. Also I doubt anyone's ever gonna read this anyway aha...

## Current planned features
- Rename a series of files in a single folder into a standardised format
- Cool looking GUI that will be user friendly and allow the user to intuitively use the application
- User will enter the series name and the season number
  - The program may also have the option to autofill these fields by looking at the parent directory names
- The program will then display the current list of file names adjacent to the new file names they will be changed to
- The user must be able to swap which files are renamed to which new file name, in the case of unordered files
  - This will be done through the GUI by dragging and dropping different file output names to match against their input file names
- The option will exist for double episodes to be labelled, so these can be correctly renamed
  - Do I need an option for triple episodes? Do those even exist?? Probably not so I'll leave it at double episodes only

## Stretch goals
- Have a custom format option

## Potential problems
- Going to have to see if there's a way my program can reserve these files so other programs of the user can't touch them in the midst of renaming to ensure it all works correctly
