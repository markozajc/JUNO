
# JUNO
JUNO is a UNO framework for Java that includes the standard UNO deck, lots of extensibility and out-of-the-box implementation of hands, game flow and more.

## Installation
JUNO is published to the Maven central and can be obtained by adding the following lines to your `pom.x

    <dependency>
        <groupId>com.github.markozajc</groupId>
        <artifactId>juno</artifactId>
        <version>VERSION</version>
    </dependency>
    
and replacing `VERSION` with the following version: TODO version shield from bintray.

## Usage
JUNO consists of multiple modules; UnoGame, UnoDeck, UnoPile, UnoHand, and UnoCard. You must correctly use those to implement JUNO into your code.

### UnoHand
Let's get the elephant out of the room first: the UnoHand. UnoHand holds hands and competes against the other UnoHand in a game. There are two automatic (""AI"") implementations of UnoHand (the StrategicUnoHand and the experimental PureRandomUnoHand) as well as a human-driven one (the StreamUnoHand).

You will most likely be using the StrategicUnoHand (the more strategic of the two hands) and a custom implementation that will let the user use the hand.

#### Implementing UnoHand
UnoHand implementation is pretty straightforward; you need to implement the abstract class UnoHand and its two methods; `#playCard(UnoGame, boolean)` and `#chooseColor(UnoGame)`.

The first method, `#playCard(UnoGame, boolean)` is responsible for selecting and returning a UnoCard to place on the discard pile. This is the method where you provide the user with the list of their cards, the top card of the discard pile, the information aboutthe other hand and so on. Of course, this method may also be completely automated (as with the StragicUnoHand and the PureRandomUnoHand). Do note, however, that illegal placements and placements of cards that the hand doesn't possess will be rejected by the UnoGame (unless you specifically change it not to do so, which isn't recommended). You may help yourself with the `UnoUtils` class, specifically the `analyzePossibleCards(UnoCard,List)` to reject incorrect card from the UnoHand itself, which is advised, and guide the user.

As you might have noticed, `#playCard(UnoGame, boolean)` also has the second argument, a `boolean` named "drawn". This boolean indicates whether a hand has already drawn a card and must now decide whether to place the newly-drawn card. StreamUnoHand will, for example, change "Draw a card" to "Pass" when this argument is `true`.

The synopsis of implementing the UnoHand is
	
```java public class MyUnoHand extends UnoHand {

	public MyUnoHand(String name) {
		super(name);
	}
	// Defines the constructor. You can also hard-code in the name if you wish

	@Override
	public UnoCard playCard(UnoGame game, boolean drawn) {
		UnoCard top = game.discard.getTop();
		// Get the top card of the discard pile
		
		List<UnoCard> possible = UnoUtils.analyzePossibleCards(top, this.cards);
		// Get the list of possible placements

		UnoCard picked;
		
		while(picked == null) {
			UnoCard userChoice = letUserPickACard();
			// Let the user pick a card (letUserPickACard() is just an imaginary method - you will have to make your own)
			
			if(possible.contains(userChoice)) {
				picker = userChoice;
			
			} else {
				// Tell the user that this card can not be placed
			}
		}
		
		
		return Spicked;
		// Place the card
	}

	@Override
	public UnoCardColor chooseColor(UnoGame game) {
		UnoCardColor picked;
		
		while(picked == null) {
			UnoCardColor userChoice = letUserPickAColor();
			// Let the user pick a color (letUserPickAColor() is just an imaginary method - you will have to make your own)
			
			if(userChoice.equals(UnoCardColor.WILD) {
				picker = userChoice;
			
			} else {
				// Tell the user that this color can not be chosen
			}
		}
	
		return picked;
	}
}
```





### UnoGame
UnoGame is the class that controls the game flow and logic. It's another class you will need to implement, although most of it is made for you already (that is, if you're satisfied with the way the out-of-the-box implementation functions). To implement it, you need to extend BasicUnoGame.

#### Implementing (Basic)UnoGame
BasicUnoGame requires you to only (optionally) extend a few status methods. It is recommended to extend them all and display their respective message to the user when they're called. Those methods are
* `onCardPlaced(UnoHand, UnoCard)` - Called when a card has been placed on the discard pile. The first argument is the hand that has placed the card and the second argument is the card that has been placed.
* `onColorChanged(UnoHand, UnoCardColor)` - Called when a hand changes the color (using a wild or draw four card). The first argument is the hand that has changed the color and the second one is the new color.
* `onPileShuffle()` - Called when the draw pile runs out of cards and the discard pile is merged (and shuffled) into it. This is not really important, but you may display it anyways.
* `onDrawCards(UnoHand, int)` - Called when a hand draws an amount of cards. The first argument is the hand that has drawn the cards and the second one is the quantity of the drawn cards.
* `onInvalidCard(UnoHand, UnoCard)` - Called when a hand attempts to place an invalid card. The first argument is the offending hand and the second one is the invalid card. This is mostly meant for debugging purposes as hands should not return invalid cards (see "Implementing UnoHand above" for tips on how to prevent that from happening).
* `onInvalidColor(UnoHand, UnoCardColor)` - Called when a hand attempts to set an invalid color. The first argument is the offending hand and the second one is the invalid color. Currently the only invalid color is "Wild" and returning it should be prevented by all UnoHand-s (similar to above).

I will not provide an example implementation as it should be pretty much obvious at this point. You may also look at ConsoleUnoGame, which is a pretty basic and clear implementation of this.

### UnoDeck and UnoCard
Those are the two classes that you don't really need to implement under most circumstances. The only reason you'd want to extend either (that I can think of) would be custom decks and/or cards that aren't included in JUNO.

#### Notes when implementing the two
* You need to extend the UnoGame directly. BasicUnoGame does not support custom decks/cards.
* Custom cards can only be of the standard colors (red, green, blue, yellow and "wild"). Conventionally you'd assign the custom cards that aren't of those colors to the "wild" color.
* When extending the UnoDeck, you __HAVE TO__ return a __MODIFIABLE__ clone of the deck in `getCards()`.
* You should not open issues for errors that occur in/due to your custom implementations.

## Specification
As you might already know, JUNO is based on the official UNO rules plus the progressive UNO official house rule, both of which can be found [here](https://service.mattel.com/instruction_sheets/UNO%20Basic%20IS.pdf).

All changes to the upstream must thus comply with the rules above with a few exception:
1. The defined objective of the game (to reach "500 points") is not present in JUNO isn't intended for more-round plays. Of course, you may implement the actual scoring system as defined in the rules in your code, but keep it out of upstream
2. There is no "dealer" so the first player to place a card is not the one "on the dealer's left" but "the first player given to the UnoGame"
3. There is no yelling "UNO!" in JUNO. An event that would signal it may be added to BasicUnoGame, but it should be done automatically as making the hands yell it really doesn't make much sense
4. The additional "hitch" to the wild draw four has not been implemented. It may be implemented, but it really just doesn't make sense to me
5. Jump-in rule can't be implemented into JUNO because JUNO isn't asynchronous and implementing a human-like delay to the automated hands doesn't seem like a great idea
6. Seven-O may be implemented, but the "seven" part of it wouldn't work because JUNO is currently a 2-player game (similar to how "reverse" and "skip" cards both do the same thing in JUNO)
7. The initial card is always a numeric card so there are no special-case scenarios for action cards