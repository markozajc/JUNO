[![Download](https://api.bintray.com/packages/iareas/Libraries/JUNO/images/download.svg) ](https://bintray.com/iareas/Libraries/JUNO/_latestVersion).
[![Build Status](https://travis-ci.org/markozajc/JUNO.svg?branch=master)](https://travis-ci.org/markozajc/JUNO)
[![Discord](https://discordapp.com/api/guilds/323031870088675328/widget.png)](https://discord.gg/asDUrbR)
# JUNO
JUNO is a UNO library for Java with lots of extensibility and flow control that comes bundled with several implementation of cards, rules and more.

It is built on top of the official UNO rules (can be found [here](https://service.mattel.com/instruction_sheets/UNO%20Basic%20IS.pdf)) and thus only comes preloaded with the official deck, rules and cards. You can, however, add your own rules and cards with its powerful systems and utilities. 

## Download and installation
Latest version: [![Download](https://api.bintray.com/packages/iareas/Libraries/JUNO/images/download.svg) ](https://bintray.com/iareas/Libraries/JUNO/_latestVersion).

JUNO is published to the Maven central and can thus be easily obtained with multiple build tools. Remember to replace `VERSION` with the version provided above.

#### Maven
Add the following to your pom.xml's dependencies section:

```xml
    <dependency>
        <groupId>com.github.markozajc</groupId>
        <artifactId>juno</artifactId>
        <version>VERSION</version>
    </dependency>
```

#### Gradle
Merge the following into your build.gradle:

```json
	dependencies {
		compile 'com.github.markozajc:juno:VERSION'
	}
	
	repositories {
		mavenCentral()
	}
```

## Usage
JUNO consists of multiple modules; the UnoPlayer, the UnoRule, the UnoGame, the UnoDeck and the UnoCard. You must correctly use and implement those to be able to make a use of JUNO in your code.

### UnoPlayer
Let's get the elephant out of the room first: the UnoPlayer. UnoPlayer owns a UnoHand that holds hands and competes against the other UnoPlayer in a game. There is an autonomous implementation of UnoPlayer included (the UnoStrategicHand) as well as a human-controlled one (the UnoStreamPlayer and its standard console implementation, the UnoConsolePlayer).

You will most likely use the UnoStrategicPlayer and a custom human-driven implementation of UnoHand in single-player games.

#### Implementing UnoPlayer
UnoPlayer implementation is pretty straightforward; you need to implement the abstract class UnoHand and its three methods; `#playCard(UnoGame, UnoPlayer)`, `#chooseColor(UnoGame)`, and `#shouldPlayDrawnCard(UnoGame, UnoCard, UnoPlayer)`.

The first method, `#playCard(UnoGame, UnoPlayer)`, is responsible for selecting and returning a UnoCard to place on the discard pile. This is the method where you (in a human-driven UnoPlayer) provide the user with the list of their cards, the top card of the discard pile, the information about the other player and its hand and so on. Of course, this method may also be completely automated (as with the UnoStrategicPlayer). Do note, however, that illegal placements and placements of cards that the player's hand doesn't possess will (should) be rejected by the UnoGame (unless you specifically change it not to do so, which isn't recommended). You may use the `UnoGameUtils` and the `UnoRuleUtils` classes, specifically the `UnoGameUtils.canPlaceCard(UnoPlayer, UnoGame, UnoCard)` to reject incorrect cards from the user.

The second method, `#chooseColor(UnoGame)`, is responsible for assigning a color mask. It will be called at the beginning of a turn if the top card is of the wild color and is closed.

The third method, `#shouldPlayDrawnCard(UnoGame, UnoCard, UnoPlayer)`, allows the UnoPlayer to place the drawn card. This method will be called when a hand requests to draw and __will only be called if the card can actually be placed__.

The synopsis of implementing the UnoPlayer is
	
```java
public class MyUnoPlayer extends UnoPlayer {

	public MyUnoPlayer(String name) {
		super(name);
	}

	@Override
	public UnoCard playCard(UnoGame game, UnoPlayer next) {
		// Display the cards to the user and let them choose one of them. Also display the amount of cards the next player has.
		// After user has decided, check whether the card can be placed or not. If yes, return it.
	}

	@Override
	public UnoCardColor chooseColor(UnoGame game) {
		// Let the user choose a color (red, green, blue or yellow) and then return the color they have decided.
	}
	
	@Override
	public boolean shouldPlayDrawnCard(UnoGame game, UnoCard drawn, UnoPlayer next) {
		// Ask the user whether to place the drawn card (drawn) or not. Return their decision as a boolean.
	}
}
```

### UnoGame
UnoGame is the class that controls the game's flow and logic. You most likely don't need to extend this class directly as UnoControlledGame (which makes use of UnoGameFlowRule for flow control) gives you most if not all flow control.

#### Extending ControlledUnoGame
ControlledUnoGame is fairly easy to extend - the only method that you have to extend is `UnoGame#onEvent(String, String...)`, which is used to send messages from the rules to the UnoGame. You just have to catch the messages that come in and log them somewhere.

### UnoRule
A UnoRule defines rules in the game. Rules also control the flow of the game itself in a UnoControlledGame. JUNO comes preloaded with the official set of UNO rules as well as the Progressive UNO and SevenO house rules, which can be optionally activated in UnoOfficialRules with `.getPack(UnoHouseRule...)` or `.getPack(Collection<UnoHouseRule>)`. UnoRule comes in two variants - the UnoCardPlacementRule and the UnoGameFlowRule. You do not need to extend the rules unless you want to add custom behavior that is not supported by the official UNO rules/home rules

#### UnoCardPlacementRule
A UnoCardPlacement rule defines what cards can be placed on top of what cards. It works in all UnoGame implementations that use `UnoRuleUtils.combinedPlacementAnalysis(UnoCard, Collection<UnoCard>, UnoRulePack, UnoHand)`. Implementing it is pretty simple - the only thing you need to override is `#canBePlaced(UnoCard, UnoCard, UnoHand)`. This method returns a PlacementClearance enum, which decides whether the second UnoCard (card) can be placed on top of the first one (target). PlacementClearance has 3 values - ALLOWED, NEUTRAL and PROHIBITED (look at their respective javadocs for more information about them).

#### UnoGameFlowRule
A UnoGameFlowRule defines the flow of the game. It is a bit trickier to implement than UnoCardPlacementRule as it requires a more direct connection with the UnoHand. This rule type will only work in a UnoControlledGame and its implementations. You can override two methods, each of them representing a phase in a turn - `initializationPhase(UnoPlayer, UnoGame)` and `decisionPhase(UnoPlayer, UnoGame, UnoCard)`.

The initialization phase is used for closing and executing open cards as well as for other initialization tasks. Its return type is a UnoInitializationConclusion which allows you to repeat the whole phase again (in case something goes wrong) or skip the player's turn.

The decision place is used to get the decided card to the discard pile as well as some other stuff. You need to be careful not to conflict with other rules here as the order in which they are executed is not guaranteed.

#### UnoRule conflicts
UnoRule also has a conflict system with a few different conflict resolution options. To make use of it, override `UnoRule#conflictsWith(UnoRule)`, then check whether the first argument is an instance of the UnoRule your rule conflicts with. If it is, return your preferred ConflictResolution - FAIL, REPLACE or BACKOFF (look at each of the values' javadoc for more details on what each of them does). You must also make sure to call `UnoRulePack#resolveConflicts` and use the returned rule pack to resolve conflicts in your rule pack.

### UnoDeck
UnoDeck is the class that provides an initial set of cards that are copied and distributed among the piles when a game starts. JUNO comes preloaded with the official UNO deck (consists of 108 cards), stored in UnoStandardDeck. UnoDeck implementations should have a singleton deck that is returned in `getCards()`. Do note that this deck must be a copy that you can create using `UnoDeckUtils.cloneCards(Collection<UnoCard>)`.

### UnoCard
UnoCards represent cards in a game of UNO. JUNO comes preloaded with the official UNO cards, but you may add your own if you wish. All you have to do is extend UnoCard and fill in the details. Do note that you can only use the official UNO colors - red, green, blue, yellow and wild (which indicates that a color mask can be applied to the card). If you want to use your custom cards in-game, you'll also have to create a UnoDeck (possibly based off of the official one) as you can not inject the cards in an existing UnoDeck, neither should you manually inject them in a game's card economy. You will probably also want to create a set of placement and flow rules for your cards, as the existing ones very likely don't support its intended behavior. 

## See JUNO in action
* You can try it out by compiling it and running the UnoConsoleGame. If you have Maven and Git installed, you can also do the following to run it

```bash
	git clone https://github.com/markozajc/JUNO.git juno
	cd juno
	mvn compile exec:java
```

* You can try it out on Discord using LiBot, a Discord bot that implements JUNO in its `*uno` command. You can get the bot [here](https://discordapp.com/oauth2/authorize?client_id=324989005550583808&scope=bot&permissions=3165248) or you can try it out in its official support server [![Discord](https://discordapp.com/api/guilds/323031870088675328/widget.png)](https://discord.gg/asDUrbR)

## Glossary
A list of common terms used in the code and its documentation:

* Color mask - an override color that is applied to wild colors. A color mask is temporary and can be reset with `UnoCard#reset()`
* Open/closed cards - open cards are cards of which action hasn't been executed yet (such as a draw card waiting for the other player to get penalized). Open cards get closed after their action is executed
* Turn phase - a phase of a turn in a UnoControlledGame. There are two phases in a turn (initialization and decision) and each of them has its own set of flow rules
* Invalid card (placement) - the placement that is against the defined rules. Refers to placements such as Blue 5 on Red 6, Green reverse on Red skip and so on 