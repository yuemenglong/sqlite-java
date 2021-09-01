package io.github.yuemenglong.sqlite.lemon;

import io.github.yuemenglong.sqlite.common.INext;
import io.github.yuemenglong.sqlite.util.Addr;
import io.github.yuemenglong.sqlite.util.Assert;

@SuppressWarnings("UnnecessaryReturnStatement")
public class Configlist {
  static class Global {
    Config freelist = null;      /* List of free configurations */
    Config current = null;       /* Top of list of configurations */
    Addr<Config> currentend = null;   /* Last on list of configs */
    Config basis = null;         /* Top of list of basis configs */
    Addr<Config> basisend = null;     /* End of list of basis configs */
  }

  private static final Global g = new Global();

  public static Config newConfig() {
    Config new_;
    if (g.freelist == null) {
      int i;
      int amt = 3;
      g.freelist = INext.malloc(amt, Config::new);
    }
    new_ = g.freelist;
    g.freelist = g.freelist.next;
    return new_;
  }

  public static void deleteConfig(Config old) {
    old.next = g.freelist;
    g.freelist = old;
  }

  public static void init() {
    g.current = null;
    g.currentend = new Addr<>(g, "current");
    g.basis = null;
    g.basisend = new Addr<>(g, "basis");
    Config.init();
  }

  public static void reset() {
    g.current = null;
    g.currentend = new Addr<>(g, "current");
    g.basis = null;
    g.basisend = new Addr<>(g, "basis");
    Config.clear(null);
  }

  public static Config add(Rule rp, int dot) {
    Config cfp;
    Config model = new Config();
    Assert.assertTrue(g.currentend != null);
    model.rp = rp;
    model.dot = dot;
    cfp = Config.find(model);
    if (cfp == null) {
      cfp = newConfig();
      cfp.rp = rp;
      cfp.dot = dot;
      cfp.fws = Set.setNew();
      cfp.stp = null;
      cfp.bplp = null;
      cfp.fplp = null;
      cfp.next = null;
      cfp.bp = null;
      g.currentend.set(cfp);
      g.currentend = new Addr<>(cfp, "next");
      Config.insert(cfp);
    }
    return cfp;
  }

  public static Config addbasis(Rule rp, int dot) {
    Config cfp;
    Config model = new Config();
    Assert.assertTrue(g.basisend != null);
    Assert.assertTrue(g.currentend != null);
    model.rp = rp;
    model.dot = dot;
    cfp = Config.find(model);
    if (cfp == null) {
      cfp = newConfig();
      cfp.rp = rp;
      cfp.dot = dot;
      cfp.fws = Set.setNew();
      cfp.stp = null;
      cfp.bplp = null;
      cfp.fplp = null;
      cfp.next = null;
      cfp.bp = null;
      g.currentend.set(cfp);
      g.currentend = new Addr<>(cfp, "next");
      g.basisend.set(cfp);
      g.basisend = new Addr<>(cfp, "bp");
      Config.insert(cfp);
    }
    return cfp;
  }

  public static void closure(Lemon lemp) {
    Config cfp, newcfp;
    Rule rp, newrp;
    Symbol sp, xsp;
    int i, dot;
    Assert.assertTrue(g.currentend != null);
    for (cfp = g.current; cfp != null; cfp = cfp.next) {
      rp = cfp.rp;
      dot = cfp.dot;
      if (dot >= rp.nrhs) continue;
      sp = rp.rhs[dot];
      if (sp.type == Symbol.SymbolType.NONTERMINAL) {
        if (sp.rule == null && sp != lemp.errsym) {
          Error.msg(lemp.filename, rp.line, "Nonterminal \"%s\" has no rules.",
                  sp.name);
          lemp.errorcnt++;
        }
        for (newrp = sp.rule; newrp != null; newrp = newrp.nextlhs) {
          newcfp = Configlist.add(newrp, 0);
          for (i = dot + 1; i < rp.nrhs; i++) {
            xsp = rp.rhs[i];
            if (xsp.type == Symbol.SymbolType.TERMINAL) {
              Set.setAdd(newcfp.fws, xsp.index);
              break;
            } else {
              Set.setUnion(newcfp.fws, xsp.firstset);
              if (!xsp.lambda) break;
            }
          }
          if (i == rp.nrhs) Plink.add(new Addr<>(cfp, "fplp"), newcfp);
        }
      }
    }
    return;
  }

  public static void sort() {
    // TODO
//    Msort.msort(g.current);
  }
  //
  ///* Sort the configuration list */
  //void Configlist_sort(){
  //  current = (struct config *)msort(current,&(current->next),Configcmp);
  //  currentend = 0;
  //  return;
  //}
  //
  ///* Sort the basis configuration list */
  //void Configlist_sortbasis(){
  //  basis = (struct config *)msort(current,&(current->bp),Configcmp);
  //  basisend = 0;
  //  return;
  //}
  //
  ///* Return a pointer to the head of the configuration list and
  //** reset the list */
  //struct config *Configlist_return(){
  //  struct config *old;
  //  old = current;
  //  current = 0;
  //  currentend = 0;
  //  return old;
  //}
  //
  ///* Return a pointer to the head of the configuration list and
  //** reset the list */
  //struct config *Configlist_basis(){
  //  struct config *old;
  //  old = basis;
  //  basis = 0;
  //  basisend = 0;
  //  return old;
  //}
  //
  ///* Free all elements of the given configuration list */
  //void Configlist_eat(cfp)
  //struct config *cfp;
  //{
  //  struct config *nextcfp;
  //  for(; cfp; cfp=nextcfp){
  //    nextcfp = cfp->next;
  //    assert( cfp->fplp==0 );
  //    assert( cfp->bplp==0 );
  //    if( cfp->fws ) SetFree(cfp->fws);
  //    deleteconfig(cfp);
  //  }
  //  return;
  //}
}
