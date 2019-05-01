module xorgate(x, y, f);
	input x, y;
	output f;
	wire x, y, f, nandout1, nandout2, notoutx, notouty;
	
	not notx(notoutx, x);
	not noty(notouty, y);
	nand nand1(nandout1, y, notoutx);
	nand nand2(nandout2, x, notouty);
	nand nand3(f, nandout1, nandout2);	
endmodule