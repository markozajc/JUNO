[central]: https://img.shields.io/maven-central/v/org.eu.zajc/juno.svg?label=Maven%20Central
[discord]: https://discord.com/api/guilds/323031870088675328/widget.png
![central]

> [!WARNING]
> **Relocation notice for 2.3.3 and above:**\
> JUNO's artifact has relocated from `com.github.markozajc:juno` to `org.eu.zajc:juno`. Additionally, the same change
> has been made on the base package name. You will need to change JUNO's dependency's `groupId` in your pom.xml or
> build.gradle (as shown in the installation section) and you will need to replace `com.github.markozajc.juno`
> with `org.eu.zajc.juno` in your imports.

# JUNO
JUNO is a UNO library for Java with lots of extensibility and flow control that comes bundled with several implementation of cards, rules and more.

It is built on top of the official UNO rules (can be found [here](https://service.mattel.com/instruction_sheets/UNO%20Basic%20IS.pdf)) and thus only comes preloaded with the official deck, rules and cards. You can, however, add your own rules and cards with its powerful systems and utilities. 

## Download and installation
Latest version: ![central]

JUNO is published to the Maven central and can thus be easily obtained with multiple build tools.

#### Maven
Add the following to your pom.xml's dependencies section:

```xml
<dependency>
	<groupId>org.eu.zajc</groupId>
	<artifactId>juno</artifactId>
	<version>2.4</version>
</dependency>
```

#### Gradle
Merge the following into your build.gradle:

```gradle
dependencies {
	compile 'org.eu.zajc:juno:2.4'
}

repositories {
	mavenCentral()
}
```

## Usage
JUNO consists of multiple modules; the UnoPlayer, the UnoRule, the UnoGame, the UnoDeck and the UnoCard. You must correctly use and implement those to be able to make a use of JUNO in your code.

### UnoPlayer
Let's get the elephant in the room out first: the UnoPlayer. UnoPlayer owns a UnoHand that holds hands and competes against other players in a game. There is an automated implementation of UnoPlayer included (the UnoStrategicHand) as well as a human-controlled one (the UnoStreamPlayer and its standard console implementation, the UnoConsolePlayer).

#### Implementing UnoPlayer
UnoPlayer implementation is pretty straightforward; you need to implement the abstract class UnoPlayer and its three methods; `#playCard(UnoGame)`, `#chooseColor(UnoGame)`, and `#shouldPlayDrawnCard(UnoGame, UnoCard)`.

The first method, `#playCard(UnoGame)`, is responsible for selecting and returning a UnoCard to place on the discard pile. This is the method where you (in a human-driven UnoPlayer implementation) provide the player with the list of their cards, the top card of the discard pile, the information about other players and their hand size and so on. Of course, this method may also be completely automated (as is done in UnoStrategicPlayer). Do note, however, that illegal placements and placements of cards that the player's hand doesn't possess will be rejected by the UnoGame by default. You may use the `UnoGameUtils` and the `UnoRuleUtils` classes, specifically `UnoGameUtils.canPlaceCard(UnoPlayer, UnoGame, UnoCard)`, to reject incorrect cards from the user.

The second method, `#chooseColor(UnoGame)`, is responsible for assigning a color mask. It will be called at the beginning of a turn if the top card is of the wild color.

The third method, `#shouldPlayDrawnCard(UnoGame, UnoCard)`, allows the UnoPlayer to place the drawn card. This method will be called when a hand requests to draw and __will only be called if the card can actually be placed__.

The synopsis of implementing the UnoPlayer is
	
```java
public class MyUnoPlayer extends UnoPlayer {

	public MyUnoPlayer(String name) {
		super(name);
	}

	@Override
	public UnoCard playCard(UnoGame game) {
		// Display the cards to the user and let them choose one of them. Also display the amount of cards the next player has.
		// After user has decided, check whether the card can be placed or not. If yes, return it.
	}

	@Override
	public UnoCardColor chooseColor(UnoGame game) {
		// Let the user choose a color (red, green, blue or yellow) and then return the color they have decided.
	}
	
	@Override
	public boolean shouldPlayDrawnCard(UnoGame game, UnoCard drawn) {
		// Ask the user whether to place the drawn card (drawn) or not. Return their decision as a boolean.
	}
}
```

### UnoGame
UnoGame is the class that controls the game's flow and logic. You most likely don't need to extend this class directly as UnoControlledGame, which makes use of UnoGameFlowRule for flow control, handles most of the logic for you while allowing for extensibility through rules.

#### Extending ControlledUnoGame
ControlledUnoGame is fairly easy to extend - the only method that you have to extend is `UnoGame#onEvent(String, String...)`, which is used to send messages from the rules to the UnoGame. The messages are sent as a pair of a format and arguments, which can be passed to `String.format(String, String...)` to get the complete message.

### UnoRule
A UnoRule defines rules in the game. Rules also control the flow of the game itself in a UnoControlledGame. JUNO comes preloaded with the official set of UNO rules as well as the Progressive UNO and SevenO house rules (the latter of which is deprecated and currently only works for two-player game - read its documentation for more info), which can be optionally activated in UnoOfficialRules with `.getPack(UnoHouseRule...)` or `.getPack(Collection<UnoHouseRule>)`. UnoRule comes in two variants - the UnoCardPlacementRule and the UnoGameFlowRule. You do not need to extend the rules unless you want to add custom behavior that is not supported by the official UNO rules/home rules

#### UnoCardPlacementRule
A UnoCardPlacement rule defines what cards can be placed on top of what cards. It works in all UnoGame implementations that use `UnoRuleUtils.getPlaceableCards(UnoCard, Collection<UnoCard>, UnoRulePack, UnoHand)`. Implementing it is pretty simple - the only thing you need to override is `#canBePlaced(UnoCard, UnoCard, UnoHand)`. This method returns a PlacementClearance enum, which decides whether the second UnoCard (card) can be placed on top of the first one (target). PlacementClearance has 3 values - ALLOWED, NEUTRAL and PROHIBITED (look at their respective documentation for more information about them).

#### UnoGameFlowRule
A UnoGameFlowRule defines the flow of the game. It is a bit trickier to implement than UnoCardPlacementRule as it requires a more involved connection with the UnoPlayer. This rule type will only work in a UnoControlledGame and its implementations. Two methods represent a phase of a turn - `initializationPhase(UnoPlayer, UnoGame)` and `decisionPhase(UnoPlayer, UnoGame, UnoCard)`. Additionally, `finishPhase(UnoWinner, UnoGame)` is called when the game ends.

The initialization phase is used for closing and executing open cards as well as for other initialization tasks. Its return type is a UnoInitializationConclusion which allows you to repeat the whole phase again (in case something goes wrong) or skip the player's turn.

The decision place is used to get the decided card to the discard pile as well as some other stuff. You need to be careful not to conflict with other rules here as the order in which they are executed is not guaranteed.

The finish place is used to change the winner of the game.

A game's state should not be changed directly in a rule - the return value should be used to influence it instead.

#### UnoRule conflicts
UnoRule also has a conflict system with a few different conflict resolution options. To make use of it, override `UnoRule#conflictsWith(UnoRule)`, then check whether the first argument is an instance of the UnoRule your rule conflicts with. If it is, return your preferred ConflictResolution - FAIL, REPLACE or BACKOFF (look at each of the values' javadoc for more details on what each of them does). You must also make sure to call `UnoRulePack#resolveConflicts` and use the returned rule pack to resolve conflicts in your rule pack.

### UnoDeck
UnoDeck is the class that provides an initial set of cards that are copied and distributed among the piles when a game starts. JUNO comes preloaded with the official UNO deck (consists of 108 cards), stored in UnoStandardDeck. You can easily create your own UnoDecks by creating a new UnoDeck and passing it your list of UnoCards.

### UnoCard
UnoCards represent cards in a game of UNO. JUNO comes preloaded with the official UNO cards, but you may add your own if you wish. All you have to do is extend UnoCard and fill in the details. Do note that you can only use the official UNO colors - red, green, blue, yellow and wild (which indicates that a color mask can be applied to the card). If you want to use your custom cards in-game, you'll also have to create a UnoDeck (possibly based off of the official one) as you can not inject the cards in an existing UnoDeck, neither should you manually inject them in a game's card economy. You will probably also want to create a set of placement and flow rules for your cards, as the existing ones very likely don't support its intended behavior. 

## JUNO in action
* You can try it out by compiling it and running the UnoConsoleGame. If you have Maven and Git installed, you can also do the following to run it directly

```bash
	git clone https://github.com/markozajc/JUNO.git juno
	cd juno
	mvn compile exec:java
```

* You can try it out on Discord using LiBot, a Discord bot that implements JUNO in its `*uno` command. You can get the bot [here](https://libot.eu.org/get) or you can try it out in its official support server [![discord](https://discord.com/api/guilds/323031870088675328/widget.png)](https://discord.gg/asDUrbR)

## Glossary
A list of common terms used in the code and its documentation:

* Color mask - an override color that is applied to wild colors. A color mask is temporary and can be reset with `UnoCard#reset()`
* Open/closed cards - open cards are cards of which action hasn't been executed yet (such as a draw card waiting for the other player to get penalized). Open cards get closed after their action is executed
* Turn phase - a phase of a turn in a UnoControlledGame. There are two phases in a turn (initialization and decision) and each of them has its own set of flow rules
* Invalid card (placement) - the placement that is against the defined rules. Refers to placements such as Blue 5 on Red 6, Green reverse on Red skip and so on 
