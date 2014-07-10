package modlan.parse;

import modlan.report.Severity;

import java.lang.reflect.Method;
import java.util.Stack;

/**
 * Created by midvorki on 3/10/14.
 */
public class Engine
{
    public Engine(Ctx aInCtx, Cons aInCons)
    {
        ctx = aInCtx;
        cons = aInCons;
        initCallTbl();
    }

    protected Ctx getCtx()
    {
        return ctx;
    }

    protected Cons getCons()
    {
        return cons;
    }


    public final void execute()
    {
        reportInfo("execute", "begin");
        if (null != ctx)
        {
            literals.reset();
            stack.push(new StateCtx(state, "doc-root", null));
            stack.peek().setData(invokeBeginCb(stack.peek().getText()));
            reportInfo("execute", "push: " + stack.peek());
            while (ctx.hasMore())
            {
                ctx.getNext();

                stateMachine();
                synchronized (this) { run++; }
            }
        }
        while (!stack.empty())
        {
            endState();
        }
        reportInfo("execute", "end");
    }

    private void stateMachine()
    {
        char lThisChar = ctx.getThis();
        // CHECK AND HANDLE IF END OF STATE
        if (checkHandleEndState(lThisChar))
        {
            // DO NOTHING
        }
        else if (' ' == lThisChar || '\t' == lThisChar)
        {
            // skip
        }
        else if (('\n' == lThisChar) || ('\r' == lThisChar))
        {
            // skip
        }
        // CHECK AND HANDLE IF THIS CHAR IS NEXT STATE
        else if (checkHandleNextState(lThisChar, lThisChar))
        {
        }
        else
        {
            endState();
            ctx.holdThisForNext();
            //reportInfo("stateMachine", "NO END, NO BEGIN: WTF: \'" + lThisChar + "' (0x" + Integer.toHexString(lThisChar)+ ")");
        }
    }

    private boolean hasNestedData()
    {
        boolean lRet = false;
        State lNextState = null;
        boolean lDone = false;
        while ((!lDone) && ctx.hasMore())
        {
            char lThatChar = ctx.getNext();
            switch (lThatChar)
            {
                case ' ':

                    break;
                /**
                case '\n':
                case '\r':

                    break;
                **/
                default:
                {
                    lDone = true;
                    lNextState = getNextState(lThatChar);
                    ctx.holdThisForNext();
                    if (null != lNextState)
                    {
                        System.out.println("fastforwarded to next potential state: " + lNextState);
                        lRet = lNextState.isRecursivelyAttached();
                    }
                }
            }
        }
        return lRet;
    }
    private boolean checkHandleEndState(char aInThisChar)
    {
        if (state.isEnd(aInThisChar))
        {
            // BEGIN CHANGE
            boolean lPopNow = !hasNestedData();

            if (lPopNow) // END CHANGE
            {
                endState();
            }
            return true;
        }
        return false;
    }

    private void endState()
    {
        reportInfo("endState", "POP: " + stack.peek());
        stack.peek().setData(invokeEndCb(stack.peek().getText()));
        literals.reset();
        stack.pop();
        if (!stack.empty())
        {
            state = stack.peek().getState();
            reportInfo("endState", "NEW TOP: " + stack.peek());
        }

    }
    private boolean checkHandleNextState(char aInThisChar, char aInOrigChar)
    {
        State lNextState = state;
        boolean lRet = false;
        lNextState = getNextState(aInThisChar);
        if (null != lNextState && state != lNextState)
        {
            reportInfo("checkHandleNextState", (lNextState.isSelfContained() ? "SELF CONTAINED > " : "NON-SELF-CONT > ") + "NEW STATE: '" + lNextState + "' ON CHAR: '" + aInThisChar + "'");
            if (lNextState.isSelfContained())
            {
                if (ctx.hasMore())
                {
                    ctx.getNext();
                }
                handleLiterals(lNextState);
                if (0 < literals.getSize())
                {
                    // NOTE: NO NEED TO REMEMBER DATA FOR THIS: IT'S SELF CONTAINED
                    invokeBeginCb(lNextState, literals.toString());
                    literals.reset();
                }
                lNextState = state;
            }
            else
               {
	            if (Req.NONE != lNextState.getTextR    q())
	                  {
		            // CHECK IF        TATE IS NAMED
		            if (!l       extState.i          Named())
		            {

			            // IF STATE IS           OT NAMED; ADVANCE TO THE NEXT CHAR
			            // WE NEED T           ADVANCE OVER THE CHARACTER THAT IDENTIFIED
			            //          TRANSITION AND IS NOT PART OF THE LITERAL TO FOLLOW.
			                     // FOR NAMED, FIRST           HARACTER IS NOT SPECIAL,          AND IS              ART
			                     // OF L       TERAL NAM
			            if (ctx.hasMore())
       		            {
				            ctx.getNext();
			            }       		            }
		                   andleLite          als(lNextState);
		                  // IF T    IS IS NAMED     TEM, WE NEED TO HOLD THE LAST CHAR
		            //if (state.isNamed())
		            {
			            ctx.holdThisForNext();
		            }
	            }

	            reportInfo("checkHandleNextState", "NEW STATE: " + lNextState + "(" + literals + ")");


                state = lNextState;
                String lThisString = literals.toString();
                stack.push(new StateCtx(state, lThisString, stack.peek()));
                reportInfo("checkHandleNextState", "PUSH: " + stack.peek());
                // INVOKE BEGIN CB AND REMEMBER D    TA RE    URNED                          stack.p    ek(    .setData(invokeBeginCb(lThisString));
                literals.reset();
            }
            lRet = true;
        }
	    /**
	    try
	    {
		    Thread.sleep(2000);
	    }
	    catch (Throwable e) {}**/
        return lRet;
    }

    private Data invokeBeginCb(String aInString)
    {
        return invokeBeginCb(state, aInString);
    }

    private Data invokeBeginCb(State aInState, String aInString)
    {
        reportInfo("invokeBeginCb", "[+++] invoking for " + aInState + " STRING: " + aInString);
        if (null != callTbl[aInState.getIdx()][0])
        {
            try
            {
                return (Data) callTbl[aInState.getIdx()][0].invoke(getCons(), new Object[]{aInString});
            }
            catch (Throwable e)
            {
                e.printStackTrace();
                reportDeadly("parsing " + aInState.getName() + "[" + aInString + "]", e);
            }
        }
        return null;
    }

    private Data invokeEndCb(String aInString)
    {
        return invokeEndCb(state, aInString);
    }

    private Data invokeEndCb(State aInState, String aInString)
    {
        reportInfo("invokeEndCb", "[---] invoking for " + aInState + " STRING: " + aInString);

        if (null != callTbl[aInState.getIdx()][1])
        {
            try
            {
                return (Data) callTbl[aInState.getIdx()][1].invoke(getCons(), new Object[]{aInString});
            }
            catch (Throwable e)
            {
                e.printStackTrace();
                reportDeadly("parsing " + aInState.getName() + "[" + aInString + "]", e);
            }
        }
        return null;
    }

    private State getNextState(char aInTrans)
    {
        Transition lThisTransition = TransitionTable.get(state, aInTrans);
        if (null != lThisTransition)
        {
            return lThisTransitio    .getToState();
        }
        else
        {
               return '*' == aInTrans ? null : getNextState('*');
        }
    }

    private void handleLiterals(State aInState)
    {
	    reportInfo("handleLiterals(" + aInState + ")", "begin");

	    l    terals.reset();
        c    ar lThisChar = ctx.getThis();

        // FAST FORWARD TO THE END OF NAME
        while (true)
        {
            if (aInState.isEnd(lThisChar))
            {
	            // END OF STATE
	            reportIn    o("handleLiterals(" + aInState + ")"     "DONE: @end with: " + literals);
                return
            }
            else if (null != TransitionTable.get(aInState, lThisChar))
            {
	            // NEXT STATE IS BEGI    NING
	            reportInfo("handleLiterals(" + aInState + ")",
	                       "DONE: @next state '" + TransitionTable.get(aInState, lThisChar) + "' on '" + lThisChar + "' with: " + literals);
	            return;
            }
                  else if (Character.isLetter(lThisCha    ) ||
                Character.isDigit(lThisChar))
            {
                literals.append(lThisChar);
            }
               else if ('\n' == lThisChar ||
		               '\r'       == lThisChar)
            {
	                  // SKIP
                     }
                        else if (' ' ==                   lThisChar ||
                     '\t' == lThisChar)
            {                   	            /**if (aInStat             .isSelfCont          ined())
	                        {
		            switch (aInState             getBlankInc          ())
		                     {
			                        case DISALLOW:

				                        reportDea       ly(
					                   "parsing " + a    nState.getName() + "[" + literals.toString    ) + "]",
		       			            "unexpec          ed blank space");

                			            break;

			            case SKIP:

				            //                NO PARSING OF THIS CHARACTER
          			                  break;

			                     case ALLOW:
			            defaul          :

				                  literals.appen       (lThisChar);
				                     break;
		            }          	               }
	                **/
	            switch (aInState.g    tBlankIncl())
	            {
		            case DISALLOW:

			            reportDea    ly(
					            "parsing " + aInState.getName(           + "[" + lit    rals.toString() + "]",
          				            "unexpec          ed blank space");

			            break;

		                     case SKIP:

			            // N           PARSING OF THIS CHARACTER
			            break;

		            case AL          OW:
		               default:

			            literals.append(lThisChar);
			            break;
	            }
	            //literals.append(lThisChar);
	        }
            else if (    ncl.ALLOW == aInState.getSpecialIncl())
            {
	            literals.append(lThisChar    ;
            }
			else
            {
	            reportDeadly(
			            "parsing literal",
			            "in state " + state + "/" + aInState +
			            ", special character'" + lThisChar +
			            "' are not allowed: only letters and numbers; literal so far: " +
			            literals
	                    );
            }

            if (ctx.hasMore())
            {
                lThisChar = ctx.getNext();
            }
            else
            {
	            reportInfo("handleLiterals(" + aInState + ")", "DONE: OUT OF CHARS: " + literals);
	            return;
            }
        }
    }
    public StateCtx getStateCtx()
    {
        return stack.empty() ? null : stack.peek();
    }

    private String formatLocationContext()
    {
        StringBuffer lSb = new StringBuffer();
        lSb.append('(');
        lSb.append(run);
        lSb.append(')');
        lSb.append(ctx.getFileName());
        lSb.append('(');
        lSb.append(ctx.getCurrLineNum());
        lSb.append(':');
        lSb.append(ctx.getCurrColumnNum());
        lSb.append('/');
        lSb.append(ctx.getCurrCharNum());
        lSb.append(") STATE=");
        lSb.append(state);

        return lSb.toString();
    }

    protected void reportDeadly(String aInCause, Throwable aInT)
    {
        Severity.DEATH.report(formatLocationContext(),"PARSE", aInCause, aInT);
    }

    protected void reportDeadly(String aInCause, String aInMessage)
    {
        Severity.DEATH.report(formatLocationContext(),"PARSE", aInCause, aInMessage);
    }

    protected void reportError(String aInCause, String aInMessage)
    {
        Severity.ERROR.report(formatLocationContext(),"PARSE", aInCause, aInMessage);
    }

    protected void reportWarning(String aInCause, String aInMessage)
    {
        Severity.WARN.report(formatLocationContext(),"PARSE", aInCause, aInMessage);
    }

    protected void reportInfo(String aInCause, String aInMessage)
    {
        Severity.INFO.report(formatLocationContext(),"PARSE", aInCause, aInMessage);
    }

    protected void reportTodo(String aInCause, String aInMessage)
    {
        Severity.TODO.report(formatLocationContext(),"PARSE", aInCause, aInMessage);
    }

    protected void stack(String aInCause, String aInMessage)
    {
        Severity.INFO.stack(formatLocationContext(), "PARSE", aInCause, aInMessage);
    }

    private void initCallTbl()
    {
        for (State lThisState : State.values())
        {
            if (null != lThisState.getBeginCb())
            {
                try
                {
                    callTbl[lThisState.getIdx()][0] = cons.getClass().getMethod(lThisState.getBeginCb(), new Class[]{String.class});
                }
                catch(Throwable e)
                {
                    e.printStackTrace();
                    reportDeadly("init beginCb", e);
                }
            }
            if (null != lThisState.getEndCb())
            {
                try
                {
                    callTbl[lThisState.getIdx()][1] = cons.getClass().getMethod(lThisState.getEndCb(), new Class[]{String.class});
                }
                catch(Throwable e)
                {
                    e.printStackTrace();
                    reportDeadly("init endCb", e);
                }
            }
        }
    }

    private final Ctx ctx;
    private final Cons cons;
    private final Method callTbl[][] = {
                                           { null, null,},
                                           { null, null,},
                                           { null, null },
                                           { null, null,},
                                           { null, null,},
                                           { null, null },
                                           { null, null },
                                        };
    private State state = State.DOC;
    private Stack<StateCtx> stack = new Stack<StateCtx>();

    private LitBuff literals = new LitBuff();
    int run;
}
