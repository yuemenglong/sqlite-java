package io.github.yuemenglong.sqlite.lemon;

import io.github.yuemenglong.sqlite.common.Addr;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static io.github.yuemenglong.sqlite.lemon.Common.MAXRHS;
import static io.github.yuemenglong.sqlite.lemon.Pstate.State.*;
import static io.github.yuemenglong.sqlite.common.Util.*;

public class Parse {

  @SuppressWarnings({"DuplicateExpressions", "IfCanBeSwitch"})
  public static void parseonetoken(Pstate psp) {
    String x = new String(psp.buf, psp.tokenstart, psp.tokenend - psp.tokenstart);
    byte[] b = x.getBytes();
//    String x = Strsafe.safe(psp.tokenstart);
    switch (psp.state) {
      case INITIALIZE:
        psp.prevrule = null;
        psp.preccounter = 0;
        psp.firstrule = null;
        psp.lastrule = null;
        psp.gp.nrule = 0;
      case WAITING_FOR_DECL_OR_RULE:
        if (b[0] == '%') {
          psp.state = WAITING_FOR_DECL_KEYWORD;
        } else if (islower(b[0])) {
          psp.lhs = Symbol.new_(x);
          psp.nrhs = 0;
          psp.lhsalias = null;
          psp.state = WAITING_FOR_ARROW;
        } else if (b[0] == '{') {
          if (psp.prevrule == null) {
            Error.msg(psp.filename, psp.tokenlineno,
                    "There is not prior rule opon which to attach the code " +
                            "fragment which begins on this line.");
            psp.errorcnt++;
          } else if (psp.prevrule.code != null) {
            Error.msg(psp.filename, psp.tokenlineno,
                    "Code fragment beginning on this line is not the first " +
                            "to follow the previous rule.");
            psp.errorcnt++;
          } else {
            psp.prevrule.line = psp.tokenlineno;
            psp.prevrule.code = x.substring(1);
          }
        } else if (b[0] == '[') {
          psp.state = PRECEDENCE_MARK_1;
        } else {
          Error.msg(psp.filename, psp.tokenlineno,
                  "Token \"%s\" should be either \"%%\" or a nonterminal name.",
                  x);
          psp.errorcnt++;
        }
        break;
      case PRECEDENCE_MARK_1:
        if (!isupper(b[0])) {
          Error.msg(psp.filename, psp.tokenlineno,
                  "The precedence symbol must be a terminal.");
          psp.errorcnt++;
        } else if (psp.prevrule == null) {
          Error.msg(psp.filename, psp.tokenlineno,
                  "There is no prior rule to assign precedence \"[%s]\".", x);
          psp.errorcnt++;
        } else if (psp.prevrule.precsym != null) {
          Error.msg(psp.filename, psp.tokenlineno,
                  "Precedence mark on this line is not the first " +
                          "to follow the previous rule.");
          psp.errorcnt++;
        } else {
          psp.prevrule.precsym = Symbol.new_(x);
        }
        psp.state = PRECEDENCE_MARK_2;
        break;
      case PRECEDENCE_MARK_2:
        if (b[0] != ']') {
          Error.msg(psp.filename, psp.tokenlineno,
                  "Missing \"]\" on precedence mark.");
          psp.errorcnt++;
        }
        psp.state = WAITING_FOR_DECL_OR_RULE;
        break;
      case WAITING_FOR_ARROW:
        if (b[0] == ':' && x.charAt(1) == ':' && x.charAt(2) == '=') {
          psp.state = IN_RHS;
        } else if (b[0] == '(') {
          psp.state = LHS_ALIAS_1;
        } else {
          Error.msg(psp.filename, psp.tokenlineno,
                  "Expected to see a \":\" following the LHS symbol \"%s\".",
                  psp.lhs.name);
          psp.errorcnt++;
          psp.state = RESYNC_AFTER_RULE_ERROR;
        }
        break;
      case LHS_ALIAS_1:
        if (isalpha(b[0])) {
          psp.lhsalias = x;
          psp.state = LHS_ALIAS_2;
        } else {
          Error.msg(psp.filename, psp.tokenlineno,
                  "\"%s\" is not a valid alias for the LHS \"%s\"\n",
                  x, psp.lhs.name);
          psp.errorcnt++;
          psp.state = RESYNC_AFTER_RULE_ERROR;
        }
        break;
      case LHS_ALIAS_2:
        if (b[0] == ')') {
          psp.state = LHS_ALIAS_3;
        } else {
          Error.msg(psp.filename, psp.tokenlineno,
                  "Missing \")\" following LHS alias name \"%s\".", psp.lhsalias);
          psp.errorcnt++;
          psp.state = RESYNC_AFTER_RULE_ERROR;
        }
        break;
      case LHS_ALIAS_3:
        if (b[0] == ':' && x.charAt(1) == ':' && x.charAt(2) == '=') {
          psp.state = IN_RHS;
        } else {
          Error.msg(psp.filename, psp.tokenlineno,
                  "Missing \".\" following: \"%s(%s)\".",
                  psp.lhs.name, psp.lhsalias);
          psp.errorcnt++;
          psp.state = RESYNC_AFTER_RULE_ERROR;
        }
        break;
      case IN_RHS:
        if (b[0] == '.') {
          // 1 Rule + psp.nrhs Symbol + psp.nrhs char*
          Rule rp = new Rule();
          rp.ruleline = psp.tokenlineno;
          rp.rhs = new Symbol[psp.nrhs];
          rp.rhsalias = new String[psp.nrhs];
          for (int i = 0; i < psp.nrhs; i++) {
            rp.rhs[i] = psp.rhs[i];
            rp.rhsalias[i] = psp.alias[i];
          }
          rp.lhs = psp.lhs;
          rp.lhsalias = psp.lhsalias;
          rp.nrhs = psp.nrhs;
          rp.code = null;
          rp.precsym = null;
          rp.index = psp.gp.nrule++;
          rp.nextlhs = rp.lhs.rule;
          rp.lhs.rule = rp;
          rp.next = null;
          if (psp.firstrule == null) {
            psp.firstrule = rp;
          } else {
            psp.lastrule.next = rp;
          }
          psp.lastrule = rp;
          psp.prevrule = rp;
          psp.state = WAITING_FOR_DECL_OR_RULE;
        } else if (isalpha(b[0])) {
          if (psp.nrhs >= MAXRHS) {
            Error.msg(psp.filename, psp.tokenlineno,
                    "Too many symbol on RHS or rule beginning at \"%s\".",
                    x);
            psp.errorcnt++;
            psp.state = RESYNC_AFTER_RULE_ERROR;
          } else {
            psp.rhs[psp.nrhs] = Symbol.new_(x);
            psp.alias[psp.nrhs] = null;
            psp.nrhs++;
          }
        } else if (b[0] == '(' && psp.nrhs > 0) {
          psp.state = RHS_ALIAS_1;
        } else {
          Error.msg(psp.filename, psp.tokenlineno,
                  "Illegal character on RHS of rule: \"%s\".", x);
          psp.errorcnt++;
          psp.state = RESYNC_AFTER_RULE_ERROR;
        }
        break;
      case RHS_ALIAS_1:
        if (isalpha(b[0])) {
          psp.alias[psp.nrhs - 1] = x;
          psp.state = RHS_ALIAS_2;
        } else {
          Error.msg(psp.filename, psp.tokenlineno,
                  "\"%s\" is not a valid alias for the RHS symbol \"%s\"\n",
                  x, psp.rhs[psp.nrhs - 1].name);
          psp.errorcnt++;
          psp.state = RESYNC_AFTER_RULE_ERROR;
        }
        break;
      case RHS_ALIAS_2:
        if (b[0] == ')') {
          psp.state = IN_RHS;
        } else {
          Error.msg(psp.filename, psp.tokenlineno,
                  "Missing \")\" following LHS alias name \"%s\".", psp.lhsalias);
          psp.errorcnt++;
          psp.state = RESYNC_AFTER_RULE_ERROR;
        }
        break;
      case WAITING_FOR_DECL_KEYWORD:
        if (isalpha(b[0])) {
          psp.declkeyword = x;
          psp.declargslot = null;
          psp.decllnslot = null;
          psp.state = WAITING_FOR_DECL_ARG;
          if (x.equals("name")) {
            Lemon o = psp.gp;
            psp.declargslot = new Addr<>(() -> o.name, v -> o.name = v);
          } else if (x.equals("include")) {
            Lemon o = psp.gp;
            psp.declargslot = new Addr<>(() -> o.include, v -> o.include = v);
            psp.decllnslot = new Addr<>(() -> o.includeln, v -> o.includeln = v);
          } else if (x.equals("code")) {
            Lemon o = psp.gp;
            psp.declargslot = new Addr<>(() -> o.extracode, v -> o.extracode = v);
            psp.decllnslot = new Addr<>(() -> o.extracodeln, v -> o.extracodeln = v);
          } else if (x.equals("token_destructor")) {
            Lemon o = psp.gp;
            psp.declargslot = new Addr<>(() -> o.tokendest, v -> o.tokendest = v);
            psp.decllnslot = new Addr<>(() -> o.tokendestln, v -> o.tokendestln = v);
          } else if (x.equals("token_prefix")) {
            Lemon o = psp.gp;
            psp.declargslot = new Addr<>(() -> o.tokenprefix, v -> o.tokenprefix = v);
          } else if (x.equals("syntax_error")) {
            Lemon o = psp.gp;
            psp.declargslot = new Addr<>(() -> o.error, v -> o.error = v);
            psp.decllnslot = new Addr<>(() -> o.errorln, v -> o.errorln = v);
          } else if (x.equals("parse_accept")) {
            Lemon o = psp.gp;
            psp.declargslot = new Addr<>(() -> o.accept, v -> o.accept = v);
            psp.decllnslot = new Addr<>(() -> o.acceptln, v -> o.acceptln = v);
          } else if (x.equals("parse_failure")) {
            Lemon o = psp.gp;
            psp.declargslot = new Addr<>(() -> o.failure, v -> o.failure = v);
            psp.decllnslot = new Addr<>(() -> o.failureln, v -> o.failureln = v);
          } else if (x.equals("stack_overflow")) {
            Lemon o = psp.gp;
            psp.declargslot = new Addr<>(() -> o.overflow, v -> o.overflow = v);
            psp.decllnslot = new Addr<>(() -> o.overflowln, v -> o.overflowln = v);
          } else if (x.equals("extra_argument")) {
            Lemon o = psp.gp;
            psp.declargslot = new Addr<>(() -> o.arg, v -> o.arg = v);
          } else if (x.equals("token_type")) {
            Lemon o = psp.gp;
            psp.declargslot = new Addr<>(() -> o.tokentype, v -> o.tokentype = v);
          } else if (x.equals("stack_size")) {
            Lemon o = psp.gp;
            psp.declargslot = new Addr<>(() -> o.stacksize, v -> o.stacksize = v);
          } else if (x.equals("start_symbol")) {
            Lemon o = psp.gp;
            psp.declargslot = new Addr<>(() -> o.start, v -> o.start = v);
          } else if (x.equals("left")) {
            psp.preccounter++;
            psp.declassoc = Symbol.Assoc.LEFT;
            psp.state = WAITING_FOR_PRECEDENCE_SYMBOL;
          } else if (x.equals("right")) {
            psp.preccounter++;
            psp.declassoc = Symbol.Assoc.RIGHT;
            psp.state = WAITING_FOR_PRECEDENCE_SYMBOL;
          } else if (x.equals("nonassoc")) {
            psp.preccounter++;
            psp.declassoc = Symbol.Assoc.NONE;
            psp.state = WAITING_FOR_PRECEDENCE_SYMBOL;
          } else if (x.equals("destructor")) {
            psp.state = WAITING_FOR_DESTRUCTOR_SYMBOL;
          } else if (x.equals("type")) {
            psp.state = WAITING_FOR_DATATYPE_SYMBOL;
          } else {
            Error.msg(psp.filename, psp.tokenlineno,
                    "Unknown declaration keyword: \"%%%s\".", x);
            psp.errorcnt++;
            psp.state = RESYNC_AFTER_DECL_ERROR;
          }
        } else {
          Error.msg(psp.filename, psp.tokenlineno,
                  "Illegal declaration keyword: \"%s\".", x);
          psp.errorcnt++;
          psp.state = RESYNC_AFTER_DECL_ERROR;
        }
        break;
      case WAITING_FOR_DESTRUCTOR_SYMBOL:
        if (!isalpha(b[0])) {
          Error.msg(psp.filename, psp.tokenlineno,
                  "Symbol name missing after %destructor keyword");
          psp.errorcnt++;
          psp.state = RESYNC_AFTER_DECL_ERROR;
        } else {
          Symbol sp = Symbol.new_(x);
          psp.declargslot = new Addr<>(() -> sp.destructor, v -> sp.destructor = v);
          psp.decllnslot = new Addr<>(() -> sp.destructorln, v -> sp.destructorln = v);
          psp.state = WAITING_FOR_DECL_ARG;
        }
        break;
      case WAITING_FOR_DATATYPE_SYMBOL:
        if (!isalpha(b[0])) {
          Error.msg(psp.filename, psp.tokenlineno,
                  "Symbol name missing after %destructor keyword");
          psp.errorcnt++;
          psp.state = RESYNC_AFTER_DECL_ERROR;
        } else {
          Symbol sp = Symbol.new_(x);
          psp.declargslot = new Addr<>(() -> sp.datatype, v -> sp.datatype = v);
          psp.decllnslot = null;
          psp.state = WAITING_FOR_DECL_ARG;
        }
        break;
      case WAITING_FOR_PRECEDENCE_SYMBOL:
        if (b[0] == '.') {
          psp.state = WAITING_FOR_DECL_OR_RULE;
        } else if (isupper(b[0])) {
          Symbol sp = Symbol.new_(x);
          if (sp.prec >= 0) {
            Error.msg(psp.filename, psp.tokenlineno,
                    "Symbol \"%s\" has already be given a precedence.", x);
            psp.errorcnt++;
          } else {
            sp.prec = psp.preccounter;
            sp.assoc = psp.declassoc;
          }
        } else {
          Error.msg(psp.filename, psp.tokenlineno,
                  "Can't assign a precedence to \"%s\".", x);
          psp.errorcnt++;
        }
        break;
      case WAITING_FOR_DECL_ARG:
        if (b[0] == '{' || b[0] == '\"' || isalnum(b[0])) {
          if (psp.declargslot.get() != null) {
            Error.msg(psp.filename, psp.tokenlineno,
                    "The argument \"%s\" to declaration \"%%%s\" is not the first."
//                    b[0] == '\"' ? & x[1] :x, psp.declkeyword
            );
            psp.errorcnt++;
            psp.state = RESYNC_AFTER_DECL_ERROR;
          } else {
            psp.declargslot.set(b[0] == '\"' || b[0] == '{' ? x.substring(1) : x);
            if (psp.decllnslot != null) psp.decllnslot.set(psp.tokenlineno);
            psp.state = WAITING_FOR_DECL_OR_RULE;
          }
        } else {
          Error.msg(psp.filename, psp.tokenlineno,
                  "Illegal argument to %%%s: %s", psp.declkeyword, x);
          psp.errorcnt++;
          psp.state = RESYNC_AFTER_DECL_ERROR;
        }
        break;
      case RESYNC_AFTER_RULE_ERROR:
        if (b[0] == '.') psp.state = WAITING_FOR_DECL_OR_RULE;
        break;
      case RESYNC_AFTER_DECL_ERROR:
        if (b[0] == '.') psp.state = WAITING_FOR_DECL_OR_RULE;
        if (b[0] == '%') psp.state = WAITING_FOR_DECL_KEYWORD;
        break;
    }
  }

  public static void parse(Lemon gp) throws IOException {
    Pstate ps = new Pstate();
    FileInputStream fp;
    byte[] filebuf;
    int filesize;
    int lineno;
    byte c;
    int cp;
    int nextcp;
    int startline = 0;
    ps.gp = gp;
    ps.filename = gp.filename;
    ps.errorcnt = 0;
    ps.state = INITIALIZE;
    fp = new FileInputStream(ps.filename);
    filesize = (int) new File(ps.filename).length();
    filebuf = new byte[filesize + 1];
    int len = fp.read(filebuf);
    if (len != filesize) {
      throw new RuntimeException("Length Not Equal");
    }
    filebuf[filesize] = 0;
    fp.close();
    lineno = 1;

    ps.buf = filebuf;
    byte[] b = filebuf;
    for (cp = 0; (c = b[cp]) != 0; ) {
      if (c == '\n') lineno++;
      if (isspace(c)) {
        cp++;
        continue;
      }
      if (c == '/' && b[cp + 1] == '/') {
        cp += 2;
        while ((c = b[cp]) != 0 && c != '\n') cp++;
        continue;
      }
      if (c == '/' && b[cp + 1] == '*') {
        cp += 2;
        while ((c = b[cp]) != 0 && (c != '/' || b[cp - 1] != '*')) {
          if (c == '\n') lineno++;
          cp++;
        }
        if (c != 0) cp++;
        continue;
      }
      ps.tokenstart = cp;
      ps.tokenlineno = lineno;
      if (c == '\"') {
        cp++;
        while ((c = b[cp]) != 0 && c != '\"') {
          if (c == '\n') lineno++;
          cp++;
        }
        if (c == 0) {
          Error.msg(ps.filename, startline,
                  "String starting on this line is not terminated before the end of the file.");
          ps.errorcnt++;
          nextcp = cp;
        } else {
          nextcp = cp + 1;
        }
      } else if (c == '{') {
        int level;
        cp++;
        for (level = 1; (c = b[cp]) != 0 && (level > 1 || c != '}'); cp++) {
          if (c == '\n') lineno++;
          else if (c == '{') level++;
          else if (c == '}') level--;
          else if (c == '/' && b[cp + 1] == '*') {
            int prevc;
            cp += 2;
            prevc = 0;
            while ((c = b[cp]) != 0 && (c != '/' || prevc != '*')) {
              if (c == '\n') lineno++;
              prevc = c;
              cp++;
            }
          } else if (c == '/' && b[cp + 1] == '/') {
            cp += 2;
            while ((c = b[cp]) != 0 && c != '\n') cp++;
            if (c != 0) lineno++;
          } else if (c == '\'' || c == '\"') {
            int startchar, prevc;
            startchar = c;
            prevc = 0;
            for (cp++; (c = b[cp]) != 0 && (c != startchar || prevc == '\\'); cp++) {
              if (c == '\n') lineno++;
              if (prevc == '\\') prevc = 0;
              else prevc = c;
            }
          }
        }
        if (c == 0) {
          Error.msg(ps.filename, startline,
                  "C code starting on this line is not terminated before the end of the file.");
          ps.errorcnt++;
          nextcp = cp;
        } else {
          nextcp = cp + 1;
        }
      } else if (isalnum((byte) c)) {
        while ((c = b[cp]) != 0 && (isalnum(c) || c == '_')) cp++;
        nextcp = cp;
      } else if (c == ':' && b[cp + 1] == ':' && b[cp + 2] == '=') {
        cp += 3;
        nextcp = cp;
      } else {
        cp++;
        nextcp = cp;
      }
      c = b[cp];
      b[cp] = 0;
      ps.tokenend = cp;
      parseonetoken(ps);
      b[cp] = c;
      cp = nextcp;
    }
    gp.rule = ps.firstrule;
    gp.errorcnt = ps.errorcnt;
  }
}
