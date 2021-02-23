package rs.ac.bg.etf.pp1;


import rs.ac.bg.etf.pp1.CounterVisitor.VarCounter;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.*;


public class CodeGenerator extends VisitorAdaptor {
	
	private int varCount;
	
	private int paramCnt;
	
	private int mainPc;
	
	public int getMainPc() {
		return mainPc;
	}
	
	//method entry
	public void visit(MethodTypeName methodTypeName) {
		//ukoliko je u pitanju main funkcija njenu adresu cuvamo u mainPC promenljivoj i koristimo je kao entry point naseg mj programa
		 
		 
		if ("main".equalsIgnoreCase(methodTypeName.getMethodName())) {
			mainPc = Code.pc;
		}
		//postavljamo u tabeli simbola za adresu tekuce metode trenutnu vrednost pc registra
		methodTypeName.obj.setAdr(Code.pc);
		
		// Collect arguments and local variables.
		SyntaxNode methodNode = methodTypeName.getParent();
		VarCounter varCnt = new VarCounter();
		methodNode.traverseTopDown(varCnt);
		
		//nemamo formalnih parametara funkcije za nivo A
		
		// Generate the entry.
		//enter instrukcija ima 2 operanda prvi je broj formalnih parametara, drugi broj lokalnih promenljivih,  pri obradi funkcije enter
		//alocira na procStacku dovoljno prostora za lokalne promenljive i paramete metode, pri pozivu funkcije stvarni parametri se stavljaju na expr Stack
		//call instrukcija stavlja povratnu adresu na proc stek a u pc se upisuje adresa pozvane metode
		//kada se skoci na tu metodu krece izvrsavanje njenih instrukcija i prva instrukcija koja se IZVRSAVA u pozvanoj metodi je enter
		//enter alocira prostor na steku za lokalne promenljive i parametre funkcije i kopira stvarne parametre sa expr steka u svoj aktivacioni zapis
		//pri zavrsetku metode pozivaju se instrukcije exit koja oslobadja stek od lokalnih promenljivih i vraca se na stek pozivaoca
		//return instrukcija skida povratnu adresu i stavlja je u pc, povratna vrednost funkcije se prosledjuje preko expr steka
		
		
		Code.put(Code.enter);
		Code.put(0); //broj formalnih parametara + broj lokalnih promenljivih
		Code.put(varCnt.getCount()); //broj lokalnih promenljivih
	}
	
		
	
	//na kraju metode stavljaju se instrukcije exit i return_
	//exit oslobadja stek metode od lokalnih promenljivih i vraca na aktivacioni zapis steka pozivaoca
	//return_ skida povratnu adresu sa steka i vracamo se na adresu u programu posle poziva funkcije
	
	public void visit(MethodDecl methodDecl) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	
	//poziv metode instrukcija call s gde je s displacment=pomeraj od trenutne vrednosti pc-a do adrese pozivane funkcije
	//call instrukcija u toku izvrsavanja dodaje vrednost operanda na tekucu vrednost pc-ja i tako se skace na funkciju
	//displacement je velicine 2 B, a velicina opcode-a instrukcije call je 1B
	
	public void visit(FunctionCall functionCall) {
		Code.put(Code.call);
		Code.put2(functionCall.getDesignator().obj.getAdr()-Code.pc + 1);
		
	}
	
	
	
	//PRINT STATEMENT 
	public void visit(PrintStmt printStmt) {
		//na Expr steku se u trenutku obilaska PrintStmt cvora vec nalazi vrednost Expr
		
		Struct printExpressionType = printStmt.getExpr().struct;
		if(printExpressionType.getKind() == Struct.Array) {
			//ako je izraz u print statement-u element niza
			Struct printExpressionElemType=printExpressionType.getElemType();
			if(printExpressionElemType == Tab.intType) {
				//ako je elemnt niza tipa int=4B
				
				//stavljamo sirinu za int 5
				Code.put(Code.const_5);
				Code.put(Code.print);
				
				
			}else if(printExpressionElemType == Tab.charType) {
				//ako je element niza tipa char=1B
				
				//stavljamo sirinu za char 1
				Code.put(Code.const_1);
				Code.put(Code.bprint);
				
			}
			
		}else {
			//ako izraz u print-u nije element niza
			if(printExpressionType == Tab.intType) {
				//ako je izraz tipa int
				
				Code.put(Code.const_5);
				Code.put(Code.print);
				
			}else if(printExpressionType == Tab.charType) {
				//ako je izraz tipa char
				
				Code.put(Code.const_1);
				Code.put(Code.bprint);
				
			}
		}
	}
	
	//END PRINT STATEMENT
	
	
	//READ STATEMENT
	public void visit(ReadStmt readStmt) {
		Obj designatorObj = readStmt.getDesignator().obj;
		Struct designatorType = designatorObj.getType();
		if(designatorType == Tab.intType || (designatorType.getKind()==Struct.Array && designatorType.getElemType()==Tab.intType)) {
			Code.put(Code.read);
		}else if(designatorType == Tab.charType || (designatorType.getKind()==Struct.Array && designatorType.getElemType()==Tab.charType)) {
			Code.put(Code.bread);
		}
		
		//if(designatorObj.getKind()==Struct.Array) {
		//Obj currObj = new Obj(Obj.Elem,designatorObj.getName(),designatorType.getElemType());
		//Code.store(currObj);
		//}
		if(designatorType.getKind()==Struct.Array && designatorType.getElemType()==Tab.intType) {
			Code.put(Code.astore);
		}else if(designatorType.getKind()==Struct.Array && designatorType.getElemType()==Tab.charType) {
			Code.put(Code.bastore);
			
		}else {
			Code.store(designatorObj);
		}
		
	}
	
	//INCREMENT
	public void visit(IncrementOp incrementOp) {
		//ako je operand element niza na steku se vec nalazi adresa tog niza na heap-u i indeks elementa niza
		//ako je operand obicna promenljiva expr stack ce biti prazan 
		
		Obj designatorObj = incrementOp.getDesignator().obj;
		
		
		if(designatorObj.getType().getKind()==Struct.Array) {
			//operand je niz na steku je vec index i adresa, tj prvo adresa pa index niza
			Obj elemObj=new Obj(Obj.Elem,incrementOp.getDesignator().getName(),designatorObj.getType().getElemType());
			
			
			Code.put(Code.dup2);
			Code.load(elemObj);
			Code.put(Code.const_1);
			Code.put(Code.add);
			Code.store(elemObj);
			
		}else {
			//operand je promenljiva lokalna ili globalna
			
			Code.load(designatorObj);
			Code.put(Code.const_1);
			Code.put(Code.add);
			Code.store(designatorObj);
		}
		
	}
	
	
	//DEKREMENT
	public void visit(DecrementOp decrementOp) {
		//ako je operand element niza na steku se vec nalazi vrednost indeksa i sad treba da se stavi i vrednost adrese niza
		//ako je operand obicna promenljiva expr stack ce biti prazan
		
		Obj designatorObj = decrementOp.getDesignator().obj;
		
		
		if(designatorObj.getType().getKind()==Struct.Array) {
			//operand je niz na steku je vec index i adresa u redosledu adresa pa index, tako nam i treba
			Obj elemObj=new Obj(Obj.Elem,decrementOp.getDesignator().getName(),designatorObj.getType().getElemType());
			
			
			Code.put(Code.dup2);
			Code.load(elemObj);
			Code.put(Code.const_1);
			Code.put(Code.sub);
			Code.store(elemObj);
			
		}else {
			//operand je promenljiva lokalna ili globalna
			Code.load(designatorObj);
			Code.put(Code.const_1);
			Code.put(Code.sub);
			Code.store(designatorObj);
		}
		
	}
	
	public void visit(DesignatorArray designatorArray) {
		//expression tj indeks elementa niza ce vec biti na steku sad treba da se stavi i adresa niza na stek
		Designator parent = (Designator) designatorArray.getParent();
		Code.load(parent.obj);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		
		//posto ce biti prvo index pa adresa nama to ne odgovara pa moramo da obrnemo redosled na steku da bude adresa pa index
	}
	
	
	//ASSIGN OP
	public void visit(AssignOp assignOp) {
		Obj designatorObj=assignOp.getDesignator().obj;
		//na Expr steku se nalazi ako je dst element niza, adresa,index, pa vrednost izraza sa desne strane jednakosti
		//ako je dst promenljiva na steku se nalazi samo vrednost izraza sa desne strane jednakosti
		//dst moze biti i ime niza npr niz=new int[expr]; tada se na steku nalazi vec adresa alociranog niza u heap segmentu i ona treba da se upise kao adresa niza
		
		if(designatorObj.getType().getKind()==Struct.Array && (assignOp.getDesignator().getDesignatorOptionals() instanceof DesignatorArray)) {
			//dst je element niza
			Obj elemObj=new Obj(Obj.Elem,assignOp.getDesignator().getName(),designatorObj.getType().getElemType());
			Code.store(elemObj);
			
		}else {
			//dst je promenljiva(globalna ili lokalna ili ime niza)---> na steku se nalazi samo vrednost expr
			Code.store(designatorObj);
		}
	}
	
	
	//FACTOR NEW
	public void visit(FactorNew factorNew) {
		//na expr steku se nalazi vec velicina niza new Type[Expr]
		int size=0;
		if(factorNew.getType().struct==Tab.intType) {
			size=1;
		}else if(factorNew.getType().struct==Tab.charType) {
			size=0;
		}
		Code.put(Code.newarray);
		Code.put(size);
	}
	
	//CONST VALUES
	public void visit(ConstValueInt constValueInt) {
		//pravimo od konstantnih vrednosti objekte i pozivamo Code.load da bi se generisala odgovarajuca instrukcija koja ce u vreme izvrsavanja ovu vrednost da stavi na stek
		//u pitanju je neposredno adresiranje
		if(!(constValueInt.getParent() instanceof ConstDeclOne)) {
		Obj constValueIntObj = new Obj(Obj.Con,"constValueInt",constValueInt.struct,constValueInt.getValue(),0);
		Code.load(constValueIntObj);
	}}
	
	public void visit(ConstValueBool constValueBool) {
		if(!(constValueBool.getParent() instanceof ConstDeclOne)) {
		int boolCastedToInt=0;
		if(constValueBool.getValue().equals("true")) {
			boolCastedToInt=1;
		}else if(constValueBool.getValue().equals("false")) {
			boolCastedToInt=0;
		}
		Obj constValueBoolObj = new Obj(Obj.Con,"constValueBool",constValueBool.struct,boolCastedToInt,0);
		Code.load(constValueBoolObj);
	}}
	
	public void visit(ConstValueChar constValueChar) {
		if(!(constValueChar.getParent() instanceof ConstDeclOne)) {
		String value=constValueChar.getValue();
		Obj constValueCharObj = new Obj(Obj.Con,"constValueChar",constValueChar.struct,value.charAt(1),0);
		Code.load(constValueCharObj);
	}}
	
	public void visit(FactorConstWithNegativeSign factorConstNeg) {
		Code.put(Code.neg);
	}
	
	public void visit(FactorDesignator factorDesignator) {
		//u ovoj smeni se designator nalazi uvek u okviru nekog izraza(znaci sa desne strane ili kao argument printa pa se njegova vrednost samo cita), te samo treba da se loaduje njegova vrednost
	   //ako je designator element niza, treba da se loaduje vrednost tog elementa niza, adresa i index niza ce se u trenutku izvrsavanja vec nalaziti na expr steku
	   //pa je ovde potrebno generisati instrukciju za loadovanje elementa niza koja ce vrednost tog elementa staviti na stek
		
	  if(factorDesignator.getDesignator().getDesignatorOptionals() instanceof NoDesignatorArray) {
		  //nije element niza
		  Code.load(factorDesignator.getDesignator().obj);
	  }else if(factorDesignator.getDesignator().getDesignatorOptionals() instanceof DesignatorArray) {
		  Obj currObj= new Obj(Obj.Elem,factorDesignator.getDesignator().getName(),factorDesignator.getDesignator().obj.getType().getElemType());
		  Code.load(currObj);
	  }
		
	}
	
	public void visit(FactorDesignatorWithNegativeSign factorDesignatorNeg) {
		//u trenutku posete ovog cvora ako je designator element niza, na expr steku se vec nalaze njegova adresa i index
		//ako je
		 if(factorDesignatorNeg.getDesignator().getDesignatorOptionals() instanceof NoDesignatorArray) {
			  //nije element niza
			  Code.load(factorDesignatorNeg.getDesignator().obj);
		  }else if(factorDesignatorNeg.getDesignator().getDesignatorOptionals() instanceof DesignatorArray) {
			  Obj currObj = new Obj(Obj.Elem,factorDesignatorNeg.getDesignator().getName(),factorDesignatorNeg.getDesignator().obj.getType().getElemType());
			  Code.load(currObj);
		  }
		 Code.put(Code.neg);
	}
	
	public void visit(FactorExprWithNegativeSign factorExprNeg) {
		//u trenutku posete ovog cvora u obj kodu ce biti vec generisane instrukcije cije ce izvrsavanje rezultovati nekom vrednoscu na steku
		//sad ta vrednost sa vrha steka treba samo da se negira
		
		Code.put(Code.neg);
	}
	
	public void visit(ExpressionOptList expressionOptList) {
		if(expressionOptList.getAddop() instanceof AddopPlus) {
			Code.put(Code.add);
		}
		if(expressionOptList.getAddop() instanceof AddopMinus) {
			Code.put(Code.sub);
		}
	}
	
	public void visit(FactorOptionalList factorOptionalList) {
		if(factorOptionalList.getMulop() instanceof MulopMul) {
			Code.put(Code.mul);
		}
		if(factorOptionalList.getMulop() instanceof MulopDiv) {
			Code.put(Code.div);
		}
		if(factorOptionalList.getMulop() instanceof MulopMod) {
			Code.put(Code.rem);
		}
	}
	
	
	public void visit(ExpressionTernar expressionTernar) {
		//expr1 ? expr1 : expr1
		//uslov mora biti tipa int
		//ako je uslov razlicit od nule prihvata se prva opcija ako nije prihvata se druga opcija
		//u trenutku posete ovog cvora vec su se generisale instrukcije koje ce u vreme izvrsavanja programa na Expr stek staviti:
		//prvo vrednost uslova, pa vrednost prvog izraza i onda vrednost drugog izraza
		/*
		        ExprStack
		        ---------
		        |  cond  |               |opcija 2                |opcija 1             |opcija 1                                                       | opcija 2       |opcija 2     
		        ----------   dup_x2,pop              dup_x2,pop              const_0                                                        dup_x1                 pop              pop
		 		|opcija 1|   ------->    |cond        -------->   |opcija 2 --------->  |opcija 2 ---->case when cond==0 accepting option 2 -------->   | opcija 1 ----> |opcija 1 ------> |opcija 2
		 		---------- 
		 		|opcija 2|               |opcija 1                |cond                 |cond                                                           |opcija 2
		 		----------                        
		                                                                                |0
		                                                               
		                                                                                                                                                           pop
		                                                                                         ----->case when cond!=0 accepting option 1  ------>    |opcija 1 ------> |opcija 1
		                                                                                          
		                                                                                                                                                |opcija 2
		                                                                                                                                        
		  */ 
		
		Code.put(Code.dup_x2);
		Code.put(Code.pop);
		Code.put(Code.dup_x2);
		Code.put(Code.pop);
		
		//sada posto se uslov nalazi na vrhu setka dodajemo jos i vrednost za komparaciju u ovom slucaju 0
		Code.loadConst(0);
		
		//sada se generise kod za instrukciju skoka
		Code.putFalseJump(Code.eq, 0);   //---->generise jneq i dvobajtni ofset koji mi u ovom trenutku ne znamo pa stavljamo 0 a posle cemo zakrpiti
		int patchAddr1=Code.pc-2; //pamtimo adresu koju treba posle zakrpiti
		//ove instrukcije ispod instrukcije skoka ce se izvrsavati samo ako uslov nije ispunjen tj ako je uslov jednak 0, a ako je uslov razlicit od 0 skocicemo na odgovarajucu instrukciju koja treba da se odradi
		
		//na steku ostavljamo samo opciju 2 koja ce se dalje prenositi roditeljskim cvorovima
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.put(Code.pop);
		Code.putJump(0); //stavljamo instrukciju bezuslovnog skoka da bismo preskocili deo koda koji se izvrsava u slucaju kada je uslov ispunjen
		int patchAddr2=Code.pc-2;
		
	    Code.fixup(patchAddr1);//radimo backpatching za prvi skok
		Code.put(Code.pop);
		Code.fixup(patchAddr2);//radimo backpatching za drugi skok
		
		
		
		//??????? ne znam da li ovo moze ovako
		
	}
	
	
	
	
	
	
}


