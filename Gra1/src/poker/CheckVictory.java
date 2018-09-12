package poker;

public class CheckVictory {
	
	//hand	0-highcard	1-pair	2-2pair		3-3ofkind		4-straight		5-flush		6-fullhouse		7-4ofkind		8-straightflush
	
	public boolean[] checkVictory(PokerAI[] ais, Card[]cc){
		
		Score[] scores = new Score[ais.length+1];
		
		for(int i = 0;i<ais.length;i++){
			if(ais[i]==null)continue;
			if(ais[i].state==2)continue;
			if(i!=5)System.out.println("AI "+ais[i].id+": ");
			else System.out.println("Player: ");
			scores[i]=checkPlayer(ais[i].card1,ais[i].card2,cc);			
		}
		
		for(int i = 0;i<scores.length;i++){
			if(scores[i]==null)continue;
			for(int j = 0;j<scores.length;j++){
				if(scores[j]==null||scores[j]==scores[i]||scores[i]==null)continue;
				Score winner = compare(scores[i],scores[j]);
				if(winner==scores[i])scores[j]=null;
				if(winner==scores[j])scores[i]=null;
			}
		}
		
		boolean[] winners = new boolean[6];
		int won = 0;
		for(int i = 0;i<scores.length;i++){
			if(scores[i]==null)continue;
			won++;
		}
		if(won!=1){
			for(int i = 0;i<scores.length;i++){
				if(scores[i]==null)continue;
				if(i==5)System.out.print("and Player ");
				else System.out.print("AI"+ais[i].id+" ");;
				winners[i] = true;
			}
			System.out.println("tied");
		}else{
			for(int i = 0;i<scores.length;i++){
				if(scores[i]==null)continue;
				if(i==5){
					System.out.println("Player won");					
				}
				else{
					System.out.println("AI"+ais[i].id+" won");
				}
				winners[i] = true;
			}
		}
		return winners;
	}
	
	private Score checkPlayer(Card card1, Card card2, Card[] cc){
		
		Card[] cards = new Card[7];
		for(int i = 0;i<cc.length;i++){
			cards[i]=cc[i];
		}
		cards[5]=card1;
		cards[6]=card2;
		
		int hand = 0;
		Score ss = null;
		Score sp = null;
		Score sf = checkFlush(cards);
		if(sf!=null)hand = sf.hand;
		if(hand!=8){
			sp = checkPairs2(cards);
			if(sp!=null){
				if(sp.hand>hand)hand=sp.hand;
			}
			if(hand<4){
				ss = checkStraight(cards);
				if(ss != null){
					if(ss.hand>hand)hand = ss.hand;
				}
			}
		}
		Score s = null;
		s = compare(ss,sp);
		s = compare(s,sf);
		
		if(s==null){
			s=highCards(cards);
		}
		
		System.out.println("hand: "+s.hand);
		System.out.print("Heights: ");
		if(s.heights!=null){
			for(int i = 0;i<s.heights.length;i++){
				System.out.print(s.heights[i]+" ");
			}
		}
		System.out.println();
		System.out.print("kicker: ");
		if(s.kickers!=null){
			for(int i = 0;i<s.kickers.length;i++){
				System.out.print(s.kickers[i]+" ");
			}
		}
		System.out.println();
		System.out.println();
		
		return s;
	}
	
	/**
	 * Return better score, null if equal
	 */
	private Score compare(Score s1, Score s2){
		if(s1==null)return s2;
		if(s2==null)return s1;
		if(s1.hand>s2.hand)return s1;
		if(s1.hand<s2.hand)return s2;
		if(s1.heights!=null&s2.heights!=null){
			if(s1.heights[0]>s2.heights[0])return s1;
			if(s1.heights[0]<s2.heights[0])return s2;
			if(s1.heights.length>1&&s2.heights.length>1){
				if(s1.heights[1]>s2.heights[1])return s1;
				if(s1.heights[1]<s2.heights[1])return s2;
			}
		}
		try{
			if(s1.kickers!=null&&s2.kickers!=null){
				for(int i = 0;i<5;i++){				
					if(s1.kickers[i]>s2.kickers[i])return s1;
					if(s1.kickers[i]<s2.kickers[i])return s2;
				}
			}
		}catch(Exception ex){}
		return null;
	}
	
 	private Score highCards(Card[] cards){
		//no pair
		int hand = 0;
		//find kicker
		int[]kickers = findAllKickers(cards);
		return new Score(hand,kickers,null);
	}
	
	private Score checkFlush(Card[] cards){
		int hand = 0;
		int height = 0;
		
		for(int a = 0;a<cards.length;a++){
			Card c1 = cards[a];
			for(int b = 0;b<cards.length;b++){
				Card c2 = cards[b];
				if(c1==c2)continue;
				if(c1.sign==c2.sign){
					for(int c = 0;c<cards.length;c++){
						Card c3 = cards[c];
						if(c1==c3)continue;
						if(c2==c3)continue;
						if(c1.sign==c3.sign){
							for(int d = 0;d<cards.length;d++){
								Card c4 = cards[d];
								if(c1==c4)continue;
								if(c2==c4)continue;
								if(c3==c4)continue;
								if(c1.sign==c4.sign){
									for(int e = 0;e<cards.length;e++){
										Card c5 = cards[e];
										if(c1==c5)continue;
										if(c2==c5)continue;
										if(c3==c5)continue;
										if(c4==c5)continue;
										if(c1.sign==c5.sign){
											//flush - check if royal flush
											boolean royal = checkS(new Card[]{c1,c2,c3,c4,c5});
											if(royal){
												hand=8;
												height = findSHeight(new Card[]{c1,c2,c3,c4,c5});
												return new Score(hand,null,new int[]{height});
											}
											//flush
											hand =5;
											//find height
											int[] kickers = findAllKickers(new Card[]{c1,c2,c3,c4,c5});
											return new Score(hand,kickers,null);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		return null;
	}
	
	private Score checkStraight(Card[] cards){
		int hand = 0;
		int height = 0;
		for(int a = 0;a<cards.length;a++){
			Card c1 = cards[a];
			for(int b = 0;b<cards.length;b++){
				Card c2 = cards[b];
				if(c1==c2)continue;
				for(int c = 0;c<cards.length;c++){
					Card c3 = cards[c];
					if(c1==c3)continue;
					if(c2==c3)continue;
					for(int d = 0;d<cards.length;d++){
						Card c4 = cards[d];
						if(c1==c4)continue;
						if(c2==c4)continue;
						if(c3==c4)continue;
						for(int e = 0;e<cards.length;e++){
							Card c5 = cards[e];
							if(c1==c5)continue;
							if(c2==c5)continue;
							if(c3==c5)continue;
							if(c4==c5)continue;
							if(checkS(new Card[]{c1,c2,c3,c4,c5})){
								hand = 4;
								height = findSHeight(new Card[]{c1,c2,c3,c4,c5});
								return new Score(hand,null,new int[]{height});
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	private boolean checkS(Card[] hand){
		int first = hand[0].number;
		int second = hand[1].number;
		int third = hand[2].number;
		int fourth = hand[3].number;
		int fifth = hand[4].number;
		
		if(first<second){
			int a = first;
			first=second;
			second=a;
		}
		if(first<third){
			int a = first;
			first=third;
			third=a;
		}
		if(first<fourth){
			int a = first;
			first=fourth;
			fourth=a;
		}
		if(first<fifth){
			int a = first;
			first=fifth;
			fifth=a;
		}
		if(second<third){
			int a = second;
			second=third;
			third=a;
		}
		if(second<fourth){
			int a = second;
			second=fourth;
			fourth=a;
		}
		if(second<fifth){
			int a = second;
			second=fifth;
			fifth=a;
		}
		if(third<fourth){
			int a = third;
			third=fourth;
			fourth=a;
		}
		if(third<fifth){
			int a = third;
			third=fifth;
			fifth=a;
		}
		if(fourth<fifth){
			int a = fourth;
			fourth=fifth;
			fifth=a;
		}
		
		if(first==14){
			if(second==2&&third==3&&fourth==4&&fifth==5){
				return true;
			}
		}
		if(second==first-1){
			if(third==second-1){
				if(fourth==third-1){
					if(fifth==fourth-1){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Check height for straight and flush
	 */
	private int findSHeight(Card[] hand){
		int first = hand[0].number;
		int second = hand[1].number;
		int third = hand[2].number;
		int fourth = hand[3].number;
		int fifth = hand[4].number;
		
		if(first<second){
			first=second;
		}
		if(first<third){
			first=third;
		}
		if(first<fourth){
			first=fourth;
		}
		if(first<fifth){
			first=fifth;
		}
		
		return first;
	}
	
	private Score checkPairs(Card[] cards){
		
		int hand = 0;
		int[] heights;
		int[] kickers;
		
		//findPair
			//find3ofkind
				//find4ofkind return
				//else findfullhouse return
				//else findbetter30fkind
					//find4ofkind for different 3ofkind return
			//else 2-pair
				//3ofkind with different cards for fullhouse
			//find better pair for 2-pair
		
		for(int a = 0;a<cards.length;a++){
			Card c1 = cards[a];
			for(int b = 0;b<cards.length;b++){
				Card c2 = cards[b];
				if(c1==c2)continue;
				if(c1.number!=c2.number)continue;
				else{
					//pair - check for 3ofkind
					for(int c = 0;c<cards.length;c++){
						Card c3 = cards[c];
						if(c1==c3)continue;
						if(c2==c3)continue;
						if(c1.number!=c3.number){
							continue;
						}
						else{
							//3ofkind - check for 4ofkind
							for(int d = 0;d<cards.length;d++){
								Card c4 = cards[d];
								if(c1==c4)continue;
								if(c2==c4)continue;
								if(c3==c4)continue;
								if(c1.number!=c4.number){
									continue;									
								}else{
									//4ofkind
									hand =7;
									//find high card
									kickers = findKickers(cards,new Card[]{c1,c2,c3,c4});
									//find height
									heights = findPHeights(new Card[]{c1,c2,c3,c4});
									return new Score(hand,kickers,heights);
								}
							}
							//no 4ofkind - check for fullhouse
							for(int d = 0;d<cards.length;d++){
								Card c4 = cards[d];
								if(c1==c4)continue;
								if(c2==c4)continue;
								if(c3==c4)continue;
								for(int e = 0;e<cards.length;e++){
									Card c5 = cards[e];
									if(c1==c5)continue;
									if(c2==c5)continue;
									if(c3==c5)continue;
									if(c4==c5)continue;
									if(c4.number==c5.number){
										//fullhouse								
										hand = 6;
										//find height
										heights = findPHeights(new Card[]{c1,c2,c3,c4});
										return new Score(hand,null,heights);
									}
								}
							}
							//3ofkind
							hand = 3;
							//find high cards
							kickers = findKickers(cards,new Card[]{c1,c2,c3});
							//find height
							heights = findPHeights(new Card[]{c1,c2,c3});
							return new Score(hand,kickers,heights);
						}						
					}
					//no 3ofkind - check 2-pair and fullhouse
					for(int c = 0;c<cards.length;c++){
						Card c3 = cards[c];
						if(c1==c3)continue;
						if(c2==c3)continue;
						for(int d = 0;d<cards.length;d++){
							Card c4 = cards[d];
							if(c1==c4)continue;
							if(c2==c4)continue;
							if(c3==c4)continue;
							if(c3.number!=c4.number){
								continue;
							}else{
								//2 pair - check fullhouse
								for(int e = 0;e<cards.length;e++){
									Card c5 = cards[e];
									if(c1==c5)continue;
									if(c2==c5)continue;
									if(c3==c5)continue;
									if(c4==c5)continue;
									if(c4.number==c5.number){
										//fullhouse
										hand = 6;
										//find height
										heights = findPHeights(new Card[]{c1,c2,c3,c4,c5});
										return new Score(hand,null,heights);
									}else{
										//check third pair
										
									}
								}
								//2-pair
								hand = 2;
								//find high card
								kickers = findKickers(cards,new Card[]{c1,c2,c3,c4});
								//find heights
								heights = findPHeights(new Card[]{c1,c2,c3,c4});
								return new Score(hand,kickers,heights);
							}
						}
					}
					//pair
					hand = 1;
					//find high cards
					kickers = findKickers(cards,new Card[]{c1,c2});
					//find height
					heights = findPHeights(new Card[]{c1,c2});
					return new Score(hand,kickers,heights);
				}
			}
		}
		
		return null;
	}

	private Score checkPairs2(Card[] cards){
		int hand = 0;
		int[] heights;
		int[] kickers;
		
		Card[] firstPair = findPair(new Card[5],cards);
		if(firstPair==null)return null;
		Card[] threeOfKind = pairTo3ofkind(firstPair,cards);
		if(threeOfKind!=null){
			return handleThreeOfKind(threeOfKind,cards);			
		}
		
		Card[] otherPair = findPair(firstPair,cards);
		if(otherPair==null){
			//pair
			hand = 1;
			kickers = findKickers(cards,firstPair);
			heights = findPHeights(firstPair);
			return new Score(hand,kickers,heights);
		}else{
			Card[] threeOfKind2 = pairTo3ofkind(otherPair,cards);
			if(threeOfKind2!=null){
				return handleThreeOfKind(threeOfKind2,cards);			
			}
			Card[] thirdPair = findPair(new Card[]{firstPair[0],firstPair[1],otherPair[0],otherPair[1]},cards);
			if(thirdPair!=null){
				Card[] twoPair = new Card[4];
				int a = firstPair[0].number;
				int b = otherPair[0].number;
				int c = thirdPair[0].number;
				if(a>b){
					if(b>c){
						twoPair = new Card[]{firstPair[0],firstPair[1],otherPair[0],otherPair[1]};
					}else{
						twoPair = new Card[]{firstPair[0],firstPair[1],thirdPair[0],thirdPair[1]};
					}
				}else{
					if(a>c){
						twoPair = new Card[]{firstPair[0],firstPair[1],otherPair[0],otherPair[1]};
					}else{
						twoPair = new Card[]{otherPair[0],otherPair[1],thirdPair[0],thirdPair[1]};
					}
				}
				//2-pair
				hand = 2;
				kickers = findKickers(cards,twoPair);
				heights = findPHeights(twoPair);
				return new Score(hand,kickers,heights);
			}else{
				Card[] twoPair = new Card[]{firstPair[0],firstPair[1],otherPair[0],otherPair[1]};
				//2-pair
				hand = 2;
				kickers = findKickers(cards,twoPair);
				heights = findPHeights(twoPair);
				return new Score(hand,kickers,heights);
			}
		}
	}
	
	private Score handleThreeOfKind(Card[] threeOfKind,Card[] cards){
		int hand = 0;
		int[] heights;
		int[] kickers;
		
		Card[] fourOfKind=threeTo4ofkind(threeOfKind,cards);
		if(fourOfKind!=null){
			//4ofkind
			hand =7;
			kickers = findKickers(cards,fourOfKind);
			heights = findPHeights(fourOfKind);
			return new Score(hand,kickers,heights);
		}else{
			Card[] fullhouse=threeToFullhouse(threeOfKind,cards);
			if(fullhouse!=null){
				//fullhouse								
				hand = 6;
				heights = findPHeights(fullhouse);
				return new Score(hand,null,heights);
			}else{
				//3ofkind
				hand = 3;
				kickers = findKickers(cards,threeOfKind);
				heights = findPHeights(threeOfKind);
				return new Score(hand,kickers,heights);
			}
		}
	}
	
	private Card[] findPair(Card[] picked, Card[] cards){
		for(int i = 0;i<cards.length;i++){
			Card a = cards[i];
			if(cardInHand(a,picked))continue;
			for(int j = 0;j<cards.length;j++){
				Card b = cards[j];
				if(a==b)continue;
				if(cardInHand(b,picked))continue;
				if(a.number==b.number)return new Card[]{a,b};
			}
		}
		return null;
	}
	
	private Card[] pairTo3ofkind(Card[] pair, Card[] cards){
		for(int i = 0;i<cards.length;i++){
			Card a = cards[i];
			if(cardInHand(a,pair))continue;
			if(a.number==pair[0].number)return new Card[]{pair[0],pair[1],a};			
		}
		return null;
	}
	
	private Card[] threeTo4ofkind(Card[] threeOfKind, Card[] cards){
		for(int i = 0;i<cards.length;i++){
			Card a = cards[i];
			if(cardInHand(a,threeOfKind))continue;
			if(a.number==threeOfKind[0].number)return new Card[]{threeOfKind[0],threeOfKind[1],threeOfKind[2],a};			
		}
		return null;
	}
	
	/**
	 * Find a fullhouse for a 3ofkind with THE BEST PAIR
	 */
	private Card[] threeToFullhouse(Card[] threeOfKind, Card[] cards){
		Card[] pair = findPair(threeOfKind,cards);
		if(pair==null)return null;
		Card[] otherPair = findPair(new Card[]{threeOfKind[0],threeOfKind[1],threeOfKind[2],pair[0],pair[1]},cards);
		if(otherPair==null){
			return new Card[]{threeOfKind[0],threeOfKind[1],threeOfKind[2],pair[0],pair[1]};
		}else{
			if(otherPair[0].number>pair[0].number){
				return new Card[]{threeOfKind[0],threeOfKind[1],threeOfKind[2],otherPair[0],otherPair[1]};
			}else{
				return new Card[]{threeOfKind[0],threeOfKind[1],threeOfKind[2],pair[0],pair[1]};
			}
		}
	}
	
	private boolean cardInHand(Card card, Card[] hand){
		for(int i = 0;i<hand.length;i++){
			if(hand[i]==null)continue;
			if(card.number==hand[i].number&&card.sign==hand[i].sign)return true;
		}
		return false;
	}
	
	/**
	 * Find heights of pair, 2pair, 3ofkind, 4ofkind, fullhouse
	 */
	private int[] findPHeights(Card[]hand){
		int first = 0;
		int second = 0;
		for(int i = 0;i<hand.length;i++){
			if(hand[i].number>=first){
				if(hand[i].number!=first)second = first;
				first = hand[i].number;
			}else if(hand[i].number>second){
				second = hand[i].number;
			}
		}
		if(second ==0){
			return new int[]{first};
		}else{
			return new int[]{first,second};
		}
	}
	
	private int[] findAllKickers(Card[] cards){
		int first = 0;
		int second = 0;
		int third = 0;
		int fourth = 0;
		int fifth = 0;
		for(int i = 0; i<cards.length;i++){
			if(cards[i].number>first){
				fifth=fourth;							
				fourth=third;						
				third=second;					
				second=first;								
				first = cards[i].number;
			}else if(cards[i].number>second){								
				fifth=fourth;						
				fourth=third;					
				third=second;				
				second=cards[i].number;				
			}else if(cards[i].number>third){										
				fifth=fourth;
				fourth=third;
				third=cards[i].number;			
			}else if(cards[i].number>fourth){	
				fifth=fourth;
				fourth=cards[i].number;		
			}else if(cards[i].number>fifth){											
				fifth=cards[i].number;					
			}
		}
		return new int[]{first,second,third,fourth,fifth};
	}
	
	private int[] findKickers(Card[] cc, Card[]hand){
		if(hand.length==4){
			int highest = 2;
			for(int a = 0;a<cc.length;a++){
				boolean same = false;
				for(int h = 0;h<hand.length;h++){
					if(cc[a]==hand[h]){
						same = true;
						break;
					}
				}
				if(same)continue;
				if(cc[a].number>highest)highest = cc[a].number;				
			}
			return new int[]{highest};
		}else if(hand.length==3){
			int highest = 0;
			int second = 0;
			for(int a = 0;a<cc.length;a++){
				boolean same = false;
				for(int h = 0;h<hand.length;h++){
					if(cc[a]==hand[h]){
						same = true;
						break;
					}
				}
				if(same)continue;
				if(cc[a].number>highest){
					second=highest;
					highest=cc[a].number;
				}else if(cc[a].number>second){
					second=cc[a].number;
				}
			}
			return new int[]{highest,second};
		}else if(hand.length==2){
			int highest = 0;
			int second = 0;
			int third = 0;
			for(int a = 0;a<cc.length;a++){
				boolean same = false;
				for(int h = 0;h<hand.length;h++){
					if(cc[a]==hand[h]){
						same = true;
						break;
					}
				}
				if(same)continue;
				if(cc[a].number>highest){
					third=second;
					second=highest;
					highest=cc[a].number;
				}else if(cc[a].number>second){
					third=second;
					second=cc[a].number;
				}else if(cc[a].number>third){
					third=cc[a].number;
				}
			}
			return new int[]{highest,second,third};
		}
		return null;
	}

}
