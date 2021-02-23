package rs.ac.bg.etf.pp1;
import org.apache.log4j.Logger;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;
import rs.etf.pp1.symboltable.visitors.*;


public class SemanticAnalyzer extends VisitorAdaptor {
	
	public static final int CURR_TYPE_NONE = 0;
	public static final int CURR_TYPE_INT = 1;
	public static final int CURR_TYPE_CHAR = 2;
	public static final int CURR_TYPE_BOOL = 3;
	
	
	int sequenceOfCalling = 0;

	boolean errorDetected = false;
	int printCallCount = 0;
	Obj currentMethod = null;
	boolean returnFound = false;
	Struct currType = null;
	int currTypeID=0;
	int currTypeOfConstValueID=0;
	String currConstValueChar="";
	int currConstValue;
	int nVars;
	int globalVariablesCount = 0;
	int localVariablesCount = 0;
	int constCount = 0;
	boolean isReturnVoid = false;
	DumpSymbolTableVisitor dumpTable = null;
	boolean mainDefined = false;
	Struct currTypeConst = Tab.noType;
	boolean isNewFactor = false;
	Struct newFactorType = null;
	boolean isNull = false;
	

	Logger log = Logger.getLogger(getClass());

	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message); 
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.info(msg.toString());
	}
	
	//******************************PROGRAM************************************************
	//Program
	public void visit(ProgramName programName) { 
		//unosimo ime programa u tabelu simbola i otvaramo njegov opseg
		programName.obj = Tab.insert(Obj.Prog, programName.getProgName(), Tab.noType);
		Tab.openScope();
	}
	
	public void visit(Program program) { 
		//cvor Program cemo posetiti tek na samom kraju obilaska stabla
		//nVars=Tab.currentScope.getnVars(); //dohvatamo broj simbola deklarisanih u trenutnom opsegu - opsegu imena programa
		Tab.chainLocalSymbols(program.getProgramName().obj);
		nVars=Tab.currentScope.getnVars();
		Tab.closeScope();
		if(!mainDefined) {
			report_error("Greska na liniji " + program.getLine() + " : program mora imati definisanu funkciju main!", null);
				
		}
		
	}
	//********************************END PROGRAM***************************************
	
	
	
	//**************************GLOBAL VARS**************************
	
	//Global variables declaration
	 public void visit(VarIdentNoError varIdent) {
		 
		 Obj varNode = Tab.currentScope.findSymbol(varIdent.getVarName());
		 if(varNode != null) {
			 //promenljiva je vec deklarisana u trenutnom opsegu!!!
		 report_error("Greska na liniji "+ varIdent.getLine() + " : globalna promenljiva sa imenom: " + varIdent.getVarName() + " je vec deklarisana.", null);
			 
		 }else {
			 if(varIdent.getVarName().equals("int") || varIdent.getVarName().equals("char") || varIdent.getVarName().equals("bool") || varIdent.getVarName().equals("null") || varIdent.getVarName().equals("eol") || varIdent.getVarName().equals("chr") || varIdent.getVarName().equals("ord")) {
				 report_error("Greska na liniji "+varIdent.getLine()+ " : upotreba rezervisanih imena pri deklarisanju promenljive: " + varIdent.getVarName(), null);
					
			 }else {
				 globalVariablesCount++;
				 
				 //promenljiva nije deklarisana i unosimo je u tabelu simbola trenutnog opsega
				 if(!(varIdent.getArrayDecl() instanceof NoArrayDeclaration)) {
					 
					 varNode = Tab.insert(Obj.Var, varIdent.getVarName(), new Struct(Struct.Array, currType));
					 dumpTable = new DumpSymbolTableVisitor();
					 dumpTable.visitObjNode(varNode);
					 report_info("Deklarisan globalni niz sa imenom: " + varIdent.getVarName() + " na liniji: " + varIdent.getLine() + " [" + dumpTable.getOutput() + "]",null);
					 
						
				 }else {
					 //report_info("Deklarisana globalna promenljiva sa imenom: " + varIdent.getVarName(), varIdent);
					 varNode = Tab.insert(Obj.Var, varIdent.getVarName(), currType);
					 //za pravilan ispis poruke
					 dumpTable = new DumpSymbolTableVisitor();
					 dumpTable.visitObjNode(varNode);
					 report_info("Deklarisana globalna promenljiva sa imenom: " + varIdent.getVarName() + " na liniji: " + varIdent.getLine() + " [" + dumpTable.getOutput() + "]",null);
					 
				 }
				 
			 }
			 
		 }
		 
	 }
	 //********************************END GLOBAL VARS**************************************
	 
	 
	 //*******************************SYMBOLIC CONSTANTS************************************
	 //Constant definition
	 public void visit(ConstDeclOne constDeclOne) { 
		 Obj varNode = Tab.find(constDeclOne.getConstName());
		 if(varNode != Tab.noObj) {
			 //vec postoji konstanta u tabeli simbola
			 report_error("Greska na liniji "+ constDeclOne.getLine() + " : konstanta sa imenom: " + constDeclOne.getConstName() + " je vec definisana!", null);
			 
		 }else {
			 if(constDeclOne.getConstName().equals("int") || constDeclOne.getConstName().equals("char") || constDeclOne.getConstName().equals("bool") || constDeclOne.getConstName().equals("null") || constDeclOne.getConstName().equals("eol") || constDeclOne.getConstName().equals("chr") || constDeclOne.getConstName().equals("ord")) {
				 report_error("Greska na liniji " + constDeclOne.getLine() + " : upotreba rezervisanih imena pri definisanju konstante: " + constDeclOne.getConstName(), null);
				return;	
			 }
			 //sve je ok moze da se ubaci u tabelu simbola ali prvo da proverimo kontekstne uslove
			 if(currTypeOfConstValueID != currTypeID) {
				 report_error("Greska na liniji "+constDeclOne.getLine()+" : tip konstante sa imenom: " + constDeclOne.getConstName() + " i tip njoj dodeljene vrednosti se razlikuju!", null);
				 
			 }else {
				 constCount++;
				 //poklapaju se tipovi moze da prodje
				 varNode = Tab.insert(Obj.Con, constDeclOne.getConstName(), currType);
				 if(currType == Tab.charType) {
					 varNode.setAdr(currConstValueChar.charAt(1));
				 }else if(currType == Tab.intType) {
					 varNode.setAdr(currConstValue);
				 }
				 dumpTable = new DumpSymbolTableVisitor();
				 dumpTable.visitObjNode(varNode);
				 report_info("Definisana konstanta sa imenom: " + constDeclOne.getConstName() + " na liniji: " + constDeclOne.getLine() + " [" + dumpTable.getOutput() + "]",null);
				 
			 }
				
		 }

	 
	 }
	 //***************************END SYMBOLIC CONSTANTS***********************************************
	 
	 
	 
	 
	 //**********************CONST VALUE*****************************
	 //Constant values visiting
	 public void visit(ConstValueInt constValueInt) { 
		 currConstValue = constValueInt.getValue();
		 currTypeOfConstValueID = CURR_TYPE_INT; 
		 currTypeConst = Tab.intType;
		 constValueInt.struct=Tab.intType;
		 
		 
		 
	 }
	 public void visit(ConstValueBool constValueBool) { 
		 currConstValue = (constValueBool.getValue().equals("true") ? 1 : 0);
		 currTypeOfConstValueID = CURR_TYPE_BOOL; 
		 currTypeConst = Tab.intType;
		 constValueBool.struct=Tab.intType;
		 
	 }
	 
	 public void visit(ConstValueChar constValueChar) { 
		 currConstValueChar = constValueChar.getValue();
		 currTypeOfConstValueID = CURR_TYPE_CHAR; 
		 currTypeConst = Tab.charType;
		 constValueChar.struct=Tab.charType;
		 
	 }
	 
	 
	 /*public void visit(ConstValue constValue) {
		 if (currTypeOfConstValueID==CURR_TYPE_INT) {
			 constValue.struct=Tab.intType;
			 
		 }
		 if (currTypeOfConstValueID==CURR_TYPE_BOOL) {
			 constValue.struct=Tab.intType;
			 
		 }
		 if (currTypeOfConstValueID==CURR_TYPE_CHAR) {
			 constValue.struct=Tab.charType;
			 
		 }
	 }*/
	 //************************** END CONST VALUE********************
	 
	 //********************LOCAL VARIABLES****************************
	 //Local variables declaration 
	 public void visit(LocalVarIdent localVarIdent) {
			 
			 Obj varNode = Tab.currentScope.findSymbol(localVarIdent.getLocalVarName());
			 if(varNode != null) {
				 //lokalna promenljiva je vec deklarisana u trenutnom opsegu!!!
			 report_error("Greska na liniji " + localVarIdent.getLine() +" : lokalna promenljiva sa imenom " + localVarIdent.getLocalVarName() + " je vec deklarisana.", null);
				 
			 }else {
				 if(localVarIdent.getLocalVarName().equals("int") || localVarIdent.getLocalVarName().equals("char") || localVarIdent.getLocalVarName().equals("bool") || localVarIdent.getLocalVarName().equals("null") || localVarIdent.getLocalVarName().equals("eol") || localVarIdent.getLocalVarName().equals("chr") || localVarIdent.getLocalVarName().equals("ord")) {
					 report_error("Greska na liniji "+ localVarIdent.getLine() +" : upotreba rezervisanih imena pri deklarisanju promenljive: " + localVarIdent.getLocalVarName(), null);
						
				 }else {
					 localVariablesCount++;
					 
					 //promenljiva nije deklarisana i unosimo je u tabelu simbola trenutnog opsega
					 if(!(localVarIdent.getArrayDecl() instanceof NoArrayDeclaration)) {
						 
						 varNode = Tab.insert(Obj.Var, localVarIdent.getLocalVarName(), new Struct(Struct.Array, currType));
						 dumpTable = new DumpSymbolTableVisitor();
						 dumpTable.visitObjNode(varNode);
						 report_info("Deklarisan lokalni niz sa imenom: " + localVarIdent.getLocalVarName() + " na liniji: " + localVarIdent.getLine() + " [" + dumpTable.getOutput() + "]",null);
						 
							
					 }else {
						 //report_info("Deklarisana globalna promenljiva sa imenom: " + varIdent.getVarName(), varIdent);
						 varNode = Tab.insert(Obj.Var, localVarIdent.getLocalVarName(), currType);
						 //za pravilan ispis poruke
						 dumpTable = new DumpSymbolTableVisitor();
						 dumpTable.visitObjNode(varNode);
						 report_info("Deklarisana lokalna promenljiva sa imenom: " + localVarIdent.getLocalVarName() + " na liniji: " + localVarIdent.getLine() + " [" + dumpTable.getOutput() + "]",null);
						 
					 }
				 }
				 
			 }
			 
		 }
	 //*********************************END LOCAL VARS****************************
	 
	 
	 
	
	 
	 //********************TYPE*********************
	 public void visit(Type type) { 
		 //report_info("-----------------Type redni broj poziva: "+sequenceOfCalling,null);
		// sequenceOfCalling++;
		 
		 
		currType = Tab.noType;
		currTypeID=CURR_TYPE_NONE;
		Obj typeNode = Tab.find(type.getTypeName());
		if(typeNode == Tab.noObj) {
			
			if(type.getTypeName().equals("bool")) {
				//ako je tip bool posto ga nema u tabeli simbola medju standardnim tipovima posmatramo ga kao intType gde je true 1 a false 0
				type.struct = Tab.intType;
				currType = Tab.intType;
				currTypeID=CURR_TYPE_BOOL;
			}else {
				report_error("Greska na liniji " + type.getLine() + " : nije pronadjen tip " + type.getTypeName() + " u tabeli simbola! ", null);
	    		type.struct = Tab.noType;
	    		currType=Tab.noType;
	    		currTypeID=CURR_TYPE_NONE;
				
			}
		}else {
			//pronadjen tip u tabeli simbola
			if(typeNode.getKind()==Obj.Type) {
				type.struct = typeNode.getType();
				currType=typeNode.getType();
				if(currType == Tab.intType) {
					currTypeID=CURR_TYPE_INT;
				}else {
					currTypeID=CURR_TYPE_CHAR;
				}
			}else {
				report_error("Greska na liniji " + type.getLine() + " : ime " + type.getTypeName() + " ne predstavlja tip!", type);
    			type.struct = Tab.noType;
    			currType=Tab.noType;
    			currTypeID=CURR_TYPE_NONE;
			}
		}
	}
	//********************END TYPE*********************
	 
	 
	 //***************************METHODS****************************
	 
	 //Method declarations
	 public void visit(MethodTypeName methodTypeName) { 
		 //report_info("-----------------MethodTypeName redni broj poziva: "+sequenceOfCalling,null);
		 //sequenceOfCalling++;
		 
		 Obj methodNode = Tab.find(methodTypeName.getMethodName());
		 if(methodNode != Tab.noObj) {
			 //greska vec deklarisan metod sa tim nazivom
			  report_error("Greska na liniji "+ methodTypeName.getLine() + " : metoda sa imenom " + methodTypeName.getMethodName() + " je vec definisana!", null);
				
		 }else {
			 //ok
			 
			 
			 Struct retTypeStruct = null;
			 if(isReturnVoid){
				 retTypeStruct = Tab.noType;
				 isReturnVoid=false;
			 }else {
				 retTypeStruct = currType;
				 //report_error("Current type struct of method is: "+retTypeStruct.toString(),null);
			 }
			 if(methodTypeName.getMethodName().equals("main") && retTypeStruct!=Tab.noType) {
				 report_error("Greska na liniji " + methodTypeName.getLine() + " : funkcija main mora imati povratni tip void! ", null);
					
			 }
			 if(methodTypeName.getMethodName().equals("main")) {
				 mainDefined=true;
			 }
			 
			 currentMethod = Tab.insert(Obj.Meth, methodTypeName.getMethodName(),retTypeStruct);
			 methodTypeName.obj=currentMethod;
			 Tab.openScope();
			 
			 dumpTable = new DumpSymbolTableVisitor();
			 dumpTable.visitObjNode(currentMethod);
			 report_info("Obradjuje se funkcija: " + methodTypeName.getMethodName() + " na liniji: " + methodTypeName.getLine() + " [" + dumpTable.getOutput() + "]",null);
			 
			 }
		 }
		 
	 
	 
	 public void visit(MethodDecl methodDecl) { 
		 //report_info("-----------------MethodDecl redni broj poziva: "+sequenceOfCalling,null);
		 //sequenceOfCalling++;
		 
		 if(currentMethod != null) {
		 Tab.chainLocalSymbols(currentMethod);
	     Tab.closeScope();
		 }
	     
	 }
	 
	 
	 public void visit(RetValType retValType) { 
		 
		// report_info("-----------------RetValType redni broj poziva: "+sequenceOfCalling,null);
		// sequenceOfCalling++;
		 
			
		// System.out.println("Obradjujem ret val void");
		 if(retValType instanceof RetValVoid) {
			 //System.out.println("Obradjujem ret val void");
			 //report_info("Obradjuje se void",null);
			 isReturnVoid = true;
		 }
		 
	 }
	 
	 public void visit(RetValVoid retVoid) {
		 //report_info("-----------------RetValVoid redni broj poziva: "+sequenceOfCalling,null);
		 //sequenceOfCalling++;
		 //report_info("Obradjuje se void",null);
		 isReturnVoid = true;
	 }
	 //*******************************END METHODS****************************
	 
	 
	 //**********************DESIGNATOR**************************
	 public void visit(Designator designator) { 
		/* report_info("-----------------Designator redni broj poziva: "+sequenceOfCalling+" ime: "+designator.getName(),null);
		 sequenceOfCalling++;*/
		 
		 
		 Obj varNode = Tab.find(designator.getName());
		 
		 if(varNode==Tab.noObj) {
			 //ne postoji designator sa takvim imenom
			 report_error("Greska na liniji:" + designator.getLine() + " nije deklarisano ime: " + designator.getName(), null);
			 
		 }else {
			 if(designator.getName().equals("null")) {
				 isNull=true;
			 }
			 if(varNode.getKind()==Obj.Var) {
				 //u pitanju je pristup nizu
				 if(!(designator.getDesignatorOptionals() instanceof NoDesignatorArray)) {
					 if(varNode.getType().getKind()!=Struct.Array) {
						 report_error("Greska na liniji " + designator.getLine() + " : ime " + designator.getName() + " ne predstavlja ime niza!",null);
						
					 }else {
					 dumpTable = new DumpSymbolTableVisitor();
					 dumpTable.visitObjNode(varNode);
					 if(varNode.getLevel()==0) {
					 report_info("Pristup globalnom nizu: " + designator.getName() + " na liniji: " + designator.getLine() + " [" + dumpTable.getOutput() + "]",null);
					 }else {
					 report_info("Pristup lokalnom nizu: " + designator.getName() + " na liniji: " + designator.getLine() + " [" + dumpTable.getOutput() + "]",null);
						 	 
					 }
					 }
				}else {
					//pristup promenljivoj
					 dumpTable = new DumpSymbolTableVisitor();
					 dumpTable.visitObjNode(varNode);
					 if(varNode.getLevel()==0) {
					 report_info("Pristup globalnoj promenljivoj: " + designator.getName() + " na liniji: " + designator.getLine() + " [" + dumpTable.getOutput() + "]",null);
					 }else {
					 report_info("Pristup lokalnoj promenljivoj: " + designator.getName() + " na liniji: " + designator.getLine() + " [" + dumpTable.getOutput() + "]",null);
					 }
					
				}
			 }
			 if(varNode.getKind()==Obj.Meth) {
				 dumpTable = new DumpSymbolTableVisitor();
				 dumpTable.visitObjNode(varNode);
				 report_info("Poziv funkcije: " + designator.getName() + " na liniji: " + designator.getLine() + " [" + dumpTable.getOutput() + "]",null);
			
			 }
			 if(varNode.getKind()==Obj.Con) {
				 dumpTable = new DumpSymbolTableVisitor();
				 dumpTable.visitObjNode(varNode);
				 report_info("Pristup simbolickoj konstanti: " + designator.getName() + " na liniji: " + designator.getLine() + " [" + dumpTable.getOutput() + "]",null);
			
			 }
		 }
		 designator.obj=varNode;
		 
		 
	 }
	//**********************END DESIGNATOR**************************
	 
	 
	 
	 //****************FACTOR*******************
	 
	 //when factor is designator 
	 public void visit(FactorDesignator factorDesignator) { 
		 /*report_info("-----------------FactorDesignator redni broj poziva: "+sequenceOfCalling,null);
		 sequenceOfCalling++;*/
		 
		 
		 if(factorDesignator.getDesignator() != null) {
			 if(factorDesignator.getDesignator().obj.getType().getKind()==Struct.Array && (factorDesignator.getDesignator().getDesignatorOptionals() instanceof NoDesignatorArray)) {
				 //ne sme u izrazu da se nadje ime niza, samo element niza ili obicna promenljiva
				 report_error("Greska na liniji " + factorDesignator.getLine() + " : nedozvoljena upotreba imena niza!",null);
				 factorDesignator.struct=Tab.noType;
			 }else {
		 factorDesignator.struct = factorDesignator.getDesignator().obj.getType();
		 //log.info("**************************tip factor designatora:" + factorDesignator.struct.getKind() + " na liniji " + factorDesignator.getLine(), null);
		 }
		 }
	 }
	 
	 public void visit(FactorDesignatorWithNegativeSign factorDesignator) {
		 Obj designatorObj=Tab.noObj;
		 if(factorDesignator.getDesignator() != null) {
		 if(factorDesignator.getDesignator().obj.getType().getKind()==Struct.Array && (factorDesignator.getDesignator().getDesignatorOptionals() instanceof NoDesignatorArray)) {
			 //ne sme u izrazu da se nadje ime niza, samo element niza ili obicna promenljiva
			 report_error("Greska na liniji " + factorDesignator.getLine() + " : nedozvoljena upotreba imena niza!",null);
			 factorDesignator.struct=Tab.noType;
		 }
		 else{
			 designatorObj=factorDesignator.getDesignator().obj;
			 if(designatorObj.getType().getKind()==Struct.Array && designatorObj.getType().getElemType()!=Tab.intType) {
				 report_error("Greska na liniji " + factorDesignator.getLine() + " : znak negacije moze stajati samo ispred izraza tipa int!",null);
				 factorDesignator.struct=Tab.noType;
				 return;
			}
			if(designatorObj.getType()!=Tab.intType && designatorObj.getType().getKind()!=Struct.Array) {
				 //ne sme biti negativan znak isored designatora koji nije tipa int!
				 report_error("Greska na liniji " + factorDesignator.getLine() + " : znak negacije moze stajati samo ispred izraza tipa int!",null);
				 factorDesignator.struct=Tab.noType;
			 }else {
				 factorDesignator.struct=designatorObj.getType();
			 }
		 
		 }}
		
		 
	 }
	 
	 
	 public void visit(FactorConst factorConst) {
		 /*report_info("-----------------FactorConst redni broj poziva: "+sequenceOfCalling,null);
		 sequenceOfCalling++;*/
		 
		 //factorConst.struct=factorConst.getConstValue().struct;  --->MOZDA JE BOLJE OVAKOOOO!!!!!!!
		 
		 factorConst.struct=currTypeConst;
		 if(factorConst.struct != null) {
		 //log.info("tip factor designatora:" + factorConst.struct.getKind() + " na liniji " + factorConst.getLine(), null);
		 }
	 }
	 
	 
	  public void visit(FactorConstWithNegativeSign factorConst) {
		 
		 
		 //factorConst.struct=factorConst.getConstValue().struct;  --->MOZDA JE BOLJE OVAKOOOO!!!!!!!
		 if(currTypeConst != Tab.intType) {
			 report_error("Greska na liniji " + factorConst.getLine() + " : znak negacije moze stajati samo ispred izraza tipa int!",null);
		     factorConst.struct=Tab.noType;
		 }else {
		 factorConst.struct=currTypeConst;
		 }
		 if(factorConst.struct != null) {
		 //log.info("tip factor designatora:" + factorConst.struct.getKind() + " na liniji " + factorConst.getLine(), null);
		 }
	 }
	 
	 
	 //when factor=(Expr)
	 public void visit(FactorExpr factorExpr) {
		 
		 factorExpr.struct=factorExpr.getExpr().struct;
		
	 }
	 
     public void visit(FactorExprWithNegativeSign factorExpr) {
		 if(factorExpr.getExpr().struct != Tab.intType && factorExpr.getExpr().struct.getKind()!=Struct.Array) {
			 report_error("Greska na liniji " + factorExpr.getLine() + " : znak negacije moze stajati samo ispred izraza tipa int!",null);
			 factorExpr.struct=Tab.noType;
		 }else if(factorExpr.getExpr().struct.getKind()==Struct.Array && factorExpr.getExpr().struct.getElemType()!=Tab.intType){
			 report_error("Greska na liniji " + factorExpr.getLine() + " : znak negacije moze stajati samo ispred izraza tipa int!",null);
			 factorExpr.struct=Tab.noType;
		 }else {
		 factorExpr.struct=factorExpr.getExpr().struct;
		 }
	 }
	 
	 
	 //factor=new Type[Expr]
	 public void visit(FactorNew factorNew) {
		 factorNew.struct=new Struct(Struct.Array,factorNew.getType().struct);
		 if(factorNew.getExpr().struct != Tab.intType) {
			 report_error("Greska na liniji " + factorNew.getLine() + " : izraz u okviru new naredbe mora biti tipa int!",null);
			 //factorNew.struct=Tab.noType;
		 }
		 //report_info("*****************factor new type : "+factorNew.getType().struct.getKind(),null);
		 isNewFactor=true;
		 newFactorType = currType;
	 }
	
	 
	 public void visit(SingleFactorOptionalList singleFactorOptionalList) { 
		 singleFactorOptionalList.struct = singleFactorOptionalList.getFactor().struct;
	 }
	 
	 public void visit(FactorOptionalList factorOptionalList) {
		 Struct fol = factorOptionalList.getFactorOptList().struct;
		 Struct f = factorOptionalList.getFactor().struct;
		 if(fol.getKind()==Struct.Array) {
			 fol=fol.getElemType();
		 }
		 if(f.getKind()==Struct.Array) {
			 f=f.getElemType();
		 }
		 if(fol.equals(f) && fol==Tab.intType) {
			 factorOptionalList.struct=fol;
		 }else {
			 report_error("Greska na liniji "+ factorOptionalList.getLine()+" : nekompatibilni tipovi u izrazu sa Mulop opratorima.", null);
			 factorOptionalList.struct = Tab.noType;
		 }
		 
	 }
	//****************END FACTOR*******************
	 
	 
	 
	 //*********************TERM**********************
	 public void visit(Term term) {
		 //report_info("-----------------Term redni broj poziva: "+sequenceOfCalling,null);
		 //sequenceOfCalling++;
		 term.struct = term.getFactorOptList().struct;
	 }
	//*********************END TERM**********************
	 
	 
	//*********************EXPRESSIONS**********************
	 public void visit(SingleExpressionOptList single) {
		 single.struct=single.getTerm().struct;
	 }
	 
	 public void visit(ExpressionOptList exp) {
		 Struct exol = exp.getExprOptList().struct;
		 Struct t = exp.getTerm().struct;
		 
		 if(exol.getKind()==Struct.Array) {
			 exol=exol.getElemType();
		 }
		 if(t.getKind()==Struct.Array) {
			 t=t.getElemType();
		 }
		 if(exol.equals(t) && exol==Tab.intType) {
			 exp.struct=exol;
		 }else {
			 report_error("Greska na liniji "+ exp.getLine()+" : nekompatibilni tipovi u izrazu sa Addop operatorima.", null);
			 exp.struct = Tab.noType;
		 }
	 }
	 
	 
	public void visit(Expression exp) {
		 //report_info("-----------------Expression redni broj poziva: "+sequenceOfCalling,null);
		 //sequenceOfCalling++;
		
		 exp.struct=exp.getExprOptList().struct;
		 /*if((exp.getOptMinus() instanceof OptionalMinus) &&  (exp.getExprOptList().struct!=Tab.intType )) {
			 report_error("Greska na liniji " + exp.getLine() + " : izraz mora biti tipa int!",null);
		 }*/
		// report_info("***********************************struct izraza sa leve strane dodele je: "+exp.struct.getKind(),null);
		 
	 }
	 
	 public void visit(ExpressionSimple exp) {
		 //report_info("-----------------ExpressionSimple redni broj poziva: "+sequenceOfCalling,null);
		 //sequenceOfCalling++;
		 exp.struct=exp.getExpr1().struct;
	 }
	 
	 public void visit(ExpressionTernar exp) {
		 
		 Struct e1 = exp.getExpr11().struct;
		 Struct e2 = exp.getExpr12().struct;
		 if(e1.getKind()==Struct.Array) {
			 e1=e1.getElemType();
		 }
		 if(e2.getKind()==Struct.Array) {
			 e2=e2.getElemType();
		 }
		 if(e1.equals(e2)) {
			 exp.struct=e1;
		 }else {
			 report_error("Greska na liniji "+ exp.getLine()+" : nekompatibilni tipovi u ternarnom izrazu.", null);
			 exp.struct = Tab.noType;
		 }
	
	 }
	//*********************END EXPRESSSIONS**********************
	 
	 
	 
	 //*************************************STATEMENTS**************************************************
	 
	 
	 
	 
	 //**********DESIGNATOR STATEMENTS*******************
	 
	 //****************ASSIGN*************************
	 public void visit(AssignOp assignOp) {
		 Obj designatorObj=assignOp.getDesignator().obj;
		 Struct designatorType = designatorObj.getType();
		 Struct expressionType = assignOp.getExpr().struct;
		 
		 if(designatorObj.getKind()!=Obj.Var) {
			 report_error("Greska na liniji "+ assignOp.getLine()+" : leva strana dodele mora biti promenljiva ili element niza!", null);
			 return;
		 }
		 if(designatorType.getKind()==Struct.Array && ((assignOp.getDesignator().getDesignatorOptionals() instanceof NoDesignatorArray) && !isNewFactor)) {
			 if(isNull) {
				 isNull=false;
			 }else {
			 report_error("Greska na liniji "+ assignOp.getLine()+" : leva strana dodele moze biti ELEMENT niza ne ime niza!", null);}
			 return;
				
		 }
		 if((designatorType.getKind()!=Struct.Array || (designatorType.getKind()==Struct.Array && (assignOp.getDesignator().getDesignatorOptionals() instanceof DesignatorArray))) && isNewFactor) {
			 report_error("Greska na liniji " + assignOp.getLine() + " : leva strana dodele vrednosti moze biti samo ime niza!",null);
			 return;
		 }
		 
		 Struct s1 = designatorType;
		 Struct s2 = expressionType;
		 
		 if(designatorType.getKind()==Struct.Array) {
			 s1=designatorType.getElemType();
		 }
		 if(expressionType.getKind()==Struct.Array) {
			 s2=expressionType.getElemType();
		 }
		 if(!s2.assignableTo(s1)) {
			 report_error("Greska na liniji " + assignOp.getLine() + " : nekompatibilni tipovi podataka u operaciji dodele vrednosti!",null);
		 }
		  isNewFactor=false;
		 }
	 
	 
	 //****************INCREMENT********************************
	 public void visit(IncrementOp inc) {
		 //report_info("-----------------IncrementOp redni broj poziva: "+sequenceOfCalling,null);
		 //sequenceOfCalling++;
		 
		Obj varNode = inc.getDesignator().obj;
		 if(varNode.getKind()!=Obj.Var || (varNode.getType().getKind()==Struct.Array && (inc.getDesignator().getDesignatorOptionals() instanceof NoDesignatorArray))) {
			 report_error("Greska na liniji "+ inc.getLine()+" : operand u inkrement operaciji mora biti promenljiva ili element niza!", null);
				
		 }
		 if(varNode.getType().getKind() == Struct.Array && varNode.getType().getElemType()!=Tab.intType){
				 report_error("Greska na liniji "+ inc.getLine()+" : operand u inkrement operaciji mora biti tipa int!", null);
				
		 }else if(varNode.getType().getKind()!=Struct.Array && varNode.getType()!=Tab.intType) {
			 report_error("Greska na liniji "+ inc.getLine()+" : operand u inkrement operaciji mora biti tipa int!", null);
				
		 }
	 }
	 
	 
	 //****************DECREMENT*****************
	 public void visit(DecrementOp inc) {
		 Obj varNode = inc.getDesignator().obj;
		 if(varNode.getKind()!=Obj.Var || (varNode.getType().getKind()==Struct.Array && (inc.getDesignator().getDesignatorOptionals() instanceof NoDesignatorArray))) {
			 report_error("Greska na liniji "+ inc.getLine()+" : operand u dekrement operaciji mora biti promenljiva ili element niza!", null);
				
		 }
		 if(varNode.getType().getKind() == Struct.Array && varNode.getType().getElemType()!=Tab.intType){
				 report_error("Greska na liniji "+ inc.getLine()+" : operand u dekrement operaciji mora biti tipa int!", null);
				
		 }else if(varNode.getType().getKind()!=Struct.Array && varNode.getType()!=Tab.intType) {
			 report_error("Greska na liniji "+ inc.getLine()+" : operand u dekrement operaciji mora biti tipa int!", null);
				
		 }
	 }
	 
    
	 public void visit(DesignatorArray designatorArray) {
		 //report_info("-----------------DesignatorArray redni broj poziva: "+sequenceOfCalling,null);
		// sequenceOfCalling++;
		 if(designatorArray.getExpr().struct.getKind()==Struct.Array && designatorArray.getExpr().struct.getElemType()!=Tab.intType) {
			 report_error("Greska na liniji " + designatorArray.getParent().getLine() + " : izraz za indeksiranje elementa niza mora biti tipa int!",null);
			 //report_error("***************** " + designatorArray.getExpr().struct.getKind() + " : tip elementa" + designatorArray.getExpr().struct.getElemType().getKind(),null);
			 	
			 
		 }else
		 if(designatorArray.getExpr().struct != Tab.intType && designatorArray.getExpr().struct.getKind()!=Struct.Array) {
			 report_error("Greska na liniji " + designatorArray.getParent().getLine() + " : izraz za indeksiranje elementa niza mora biti tipa int!",null);
			// report_error("***************** " + designatorArray.getExpr().struct.getKind(),null);
				 
		 }
		 
	 }
	 
	 
	 //****************FUNCTION CALL*****************
	 public void visit(FunctionCall func) {
		 Obj varNode = Tab.find(func.getDesignator().getName());
		 if(varNode == Tab.noObj) {
			 report_error("Greska na liniji " + func.getLine() + " : " + func.getDesignator().getName() + " ne predstavlja ime funkcije!",null);
		 }
	 }
	 //************************************END DESIGNATOR STATEMENTS*************************************************
	 
	 

	 
	 
	 //**************READ****************
	 public void visit(ReadStmt readStmt) {
		 Obj des = readStmt.getDesignator().obj;
		 if(des.getKind()!=Obj.Var) {
			 report_error("Greska na liniji "+ readStmt.getLine() + " : argument read funkcije mora biti promenljiva ili element niza!",null);
		 }
	 }
	 
	 //**************PRINT****************
	 public void visit(PrintStmt printStmt) {
		 if(printStmt.getExpr().struct.getKind()==Struct.Array && (printStmt.getExpr().struct.getElemType()!=Tab.intType && printStmt.getExpr().struct.getElemType()!=Tab.charType)) {
			 report_error("Greska na liniji " + printStmt.getLine()+ " : argument print funkcije mora biti tipa int ili char!",null);
			return;
		 }
		 if(printStmt.getExpr().struct.getKind()!=Struct.Array && printStmt.getExpr().struct != Tab.intType && printStmt.getExpr().struct != Tab.charType) {
			 report_error("Greska na linji " + printStmt.getLine() + " : argument print funkcije mora biti tipa int ili char!",null);
		 }
	 }
	 
	//**************************END STATEMENTS**************************
	
	 
	 
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	public boolean passed() {
		return !errorDetected;
	}
	
}

