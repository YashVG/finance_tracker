# Finance Goal Setter

## Fully tested finance tracker built in Java with console UI and GUI. 

*Project goals*:

- As a user, I want to be able to create a list of recurring expenses and add anything to that list
- As a user, I wish to be able to add an income to a list of stream of incomes, and how much I'm receiving from each stream
- As a user, I wish to have a savings option where I can either:
  - allocate a certain percentage of my choosing to save from any stream of income.
  - allocate a certain percentage to save from all streams of income.
- As a user, I wish to have a comparative statistics display that shows how much I'm spending, how much I'm earning and how much I'm saving.

- As a user, I want to be able to save my list of savings
- As a user, I want to be able to save my list of expenses
- As a user, I want there to be an option to save any changes made to either list when I click back to the menu page
- As a user, I want to be able to load all my data

As shown in the UML diagram, I have two classes ListOfExpenses and ListOfSavings. These classes are extremely similar
and have almost identical methods, with one or two that are different. One thing I would do to refactor this is to use
an abstract class that both these classes can extend from, where the methods that are most similar can be inherited
from this super class, and any subclass-specific methods can be made in the subclasses themselves. This would save time
in testing, writing code, and eliminating redundancy. Not only would this improve the classes themselves, but also
the relationships with other classes, such as the UI and persistence classes.
