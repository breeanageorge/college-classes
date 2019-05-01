% Breeana George
% CSC 135 Assignment 3

% car([X|Y], X)
% cdr([X|Y], Y)
% cons(X, R, [X|R])
%------------- 1 --------------------------------
owner(fred, fido).
owner(fred, leo).
owner(fred, max).
owner(george, sport).
owner(george, bert).
owner(amy, jelly).

breed(fido, dog).
breed(leo, cat).
breed(max, dog).
breed(sport, dog).
breed(bert, dog).
breed(jelly, dog).

dogEnthusiast(N) :- breed(X, dog), breed(Y, dog), owner(N, X), owner(N, Y), X\=Y.

% ------------- 2 -------------------------------
nth(1, [Y|Z], Y).
nth(N,[X|L],Res) :- N1 is N-1, nth(N1,L,Res).

listPicker(D, [], []).
listPicker(D,[X|P],[Res|T]) :- nth(X,D,Res), listPicker(D,P,T).

% ------------- 3 -------------------------------
num(0).
num(1).
num(2).
num(3).
num(4).
num(5).
num(6).
num(7).
num(8).
num(9).
crypto(G,R,I,P,T,O,C,K) :- num(G), num(R), num(I), num(P),num(T), num(O), num(C), num(K), 
				T\=O, T\=C, T\=K, O\=C, O\=K, C\=K,
				(10000000*G+1000000*R+100000*I+10000*P+1000*T+100*O+10*C+K) =:=
				(1000*T+100*O+10*C+K) * (1000*T+100*O+10*C+K).
		

% ------------- 4 -------------------------------
interleave([], [], []).
interleave([A|X],[],[999|[A|X]]).
interleave([],[B|Y],[999|[B|Y]]).
interleave([A|X], [B|Y], [A|[B|Z]]) :-  interleave(X,Y,Z).

% ------------- 5 -------------------------------
myInc(9,0).
myInc(X,Res) :- Res is X+1.

digitinc(X,Y) :- X < 10, X > 0, myInc(X,Y).
digitinc(X,Y) :- Z is X mod 10, myInc(Z,R), W is X//10, digitinc(W, Y1), Y is (Y1*10)+R.

