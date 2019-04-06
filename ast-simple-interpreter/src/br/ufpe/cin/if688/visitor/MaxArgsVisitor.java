package br.ufpe.cin.if688.visitor;

import br.ufpe.cin.if688.ast.AssignStm;
import br.ufpe.cin.if688.ast.CompoundStm;
import br.ufpe.cin.if688.ast.EseqExp;
import br.ufpe.cin.if688.ast.Exp;
import br.ufpe.cin.if688.ast.ExpList;
import br.ufpe.cin.if688.ast.IdExp;
import br.ufpe.cin.if688.ast.LastExpList;
import br.ufpe.cin.if688.ast.NumExp;
import br.ufpe.cin.if688.ast.OpExp;
import br.ufpe.cin.if688.ast.PairExpList;
import br.ufpe.cin.if688.ast.PrintStm;
import br.ufpe.cin.if688.ast.Stm;

public class MaxArgsVisitor implements IVisitor<Integer> {
	
	private boolean inPrintStm = false;
	
	@Override
	public Integer visit(Stm s) {
		return s.accept(this);
	}

	@Override
	public Integer visit(AssignStm s) {
		return s.getExp().accept(this);
	}

	@Override
	public Integer visit(CompoundStm s) {
		int esq = s.getStm1().accept(this);
		int dir = s.getStm2().accept(this);
		return Math.max(esq, dir);
	}

	@Override
	public Integer visit(PrintStm s) {
		inPrintStm = true;
		return s.getExps().accept(this);
	}

	@Override
	public Integer visit(Exp e) {
		return e.accept(this);
	}

	@Override
	public Integer visit(EseqExp e) {
		int result;
		if (e.getStm() instanceof PrintStm) {
			result = Math.max(e.getStm().accept(this), 1 + e.getExp().accept(this));
		} else {
			result = 1 + e.getExp().accept(this);
		}
		
		return result;
	}

	@Override
	public Integer visit(IdExp e) {
		if (inPrintStm) return 1;
		return 0;
	}

	@Override
	public Integer visit(NumExp e) {
		if (inPrintStm) return 1;
		return 0;
	}

	@Override
	public Integer visit(OpExp e) {
		if (inPrintStm) return 1;
		return 0;
	}

	@Override
	public Integer visit(ExpList el) {
		return el.accept(this);
	}

	@Override
	public Integer visit(PairExpList el) {
		Integer headValue = el.getHead().accept(this);
		Integer tailValue = el.getTail().accept(this);

		return  headValue + tailValue;
	}

	@Override
	public Integer visit(LastExpList el) {
		int result = el.getHead().accept(this);
		inPrintStm = false;
		return result;
	}

}
