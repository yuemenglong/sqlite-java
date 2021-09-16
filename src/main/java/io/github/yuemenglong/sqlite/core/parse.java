/* Driver template for the LEMON parser generator.
** Copyright 1991-1995 by D. Richard Hipp.
*
* This version is specially modified for use with sqlite.
* @(#) $Id: lempar.c,v 1.1 2000/05/29 14:26:02 drh Exp $
*
**
** This library is free software; you can redistribute it and/or
** modify it under the terms of the GNU Library General Public
** License as published by the Free Software Foundation; either
** version 2 of the License, or (at your option) any later version.
**
** This library is distributed in the hope that it will be useful,
** but WITHOUT ANY WARRANTY; without even the implied warranty of
** MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
** Library General Public License for more details.
**
** You should have received a copy of the GNU Library General Public
** License along with this library; if not, write to the
** Free Software Foundation, Inc., 59 Temple Place - Suite 330,
** Boston, MA  02111-1307, USA.
**
** Modified 1997 to make it suitable for use with makeheaders.
*/
package io.github.yuemenglong.sqlite.core;

import java.io.IOException;
import java.io.OutputStream;

import static io.github.yuemenglong.sqlite.core.build.*;
import static io.github.yuemenglong.sqlite.core.delete.sqliteDeleteFrom;
import static io.github.yuemenglong.sqlite.core.insert.sqliteInsert;
import static io.github.yuemenglong.sqlite.core.select.*;
import static io.github.yuemenglong.sqlite.core.sqliteint.*;
import static io.github.yuemenglong.sqlite.core.update.sqliteUpdate;
import static io.github.yuemenglong.sqlite.core.util.sqliteSetString;

// #line 48 "D:/workspace/java/sqlite/work/parse.java"
class parse {
/* Next is all token values, in a form suitable for use by makeheaders.
** This section will be null unless lemon is run with the -m switch.
*/
/*
** These constants (all generated automatically by the parser generator)
** specify the various kinds of tokens (terminals) that the parser
** understands.
**
** Each symbol here is a terminal symbol in the grammar.
*/
/* Make sure the INTERFACE macro is defined.
*/
//#ifndef INTERFACE
//# define INTERFACE 1
//#endif
/* The next thing included is series of defines which control
** various aspects of the generated parser.
**    int         is the data type used for storing terminal
**                       and nonterminal numbers.  "unsigned char" is
**                       used if there are fewer than 250 terminals
**                       and nonterminals.  "int" is used otherwise.
**    130           is a number of type int which corresponds
**                       to no legal terminal or nonterminal number.  This
**                       number is used to fill in empty slots of the hash
**                       table.
**    int       is the data type used for storing terminal
**                       and nonterminal numbers.  "unsigned char" is
**                       used if there are fewer than 250 rules and
**                       states combined.  "int" is used otherwise.
**    Token     is the data type used for minor tokens given
**                       directly to the parser from the tokenizer.
**    YYMINORTYPE        is the data type used for all minor tokens.
**                       This is typically a union of many types, one of
**                       which is Token.  The entry in the union
**                       for base tokens is called "yy0".
**    100       is the maximum depth of the parser's stack.
**    pParse       is a declaration of a 3rd argument to the
**                       parser, or null if there is no extra argument.
**    ParseKRARGDECL     A version of pParse for K&R C.
**    Parse pParse   A version of pParse for ANSI C.
**    305           the combined number of states.
**    164            the number of rules in the grammar
**    93      is the code number of the error symbol.  If not
**                       defined, then do no error processing.
*/
/*  */
public static class YYMINORTYPE {
  public Token yy0;
  public Select yy39;
  public int yy64;
  public ExprList yy154;
  public Token yy180;
  public IdList yy204;
  public Expr yy234;
  public int yy259;
}
public static int YY_NO_ACTION     = (305+164+2);
public static int YY_ACCEPT_ACTION = (305+164+1);
public static int YY_ERROR_ACTION  = (305+164);
/* Next is the action table.  Each entry in this table contains
**
**  +  An integer which is the number representing the look-ahead
**     token
**
**  +  An integer indicating what action to take.  Number (N) between
**     0 and 305-1 mean shift the look-ahead and go to state N.
**     Numbers between 305 and 305+164-1 mean reduce by
**     rule N-305.  Number 305+164 means that a syntax
**     error has occurred.  Number 305+164+1 means the parser
**     accepts its input.
**
**  +  A pointer to the next entry with the same hash value.
**
** The action table is really a series of hash tables.  Each hash
** table contains a number of entries which is a power of two.  The
** "state" table (which follows) contains information about the starting
** point and size of each hash table.
*/
public static class yyActionEntry {
  public int   lookahead;   /* The value of the look-ahead token */
  public int action;      /* Action to take for this look-ahead */
  public int next; /* Next look-ahead with the same hash, or NULL */

  public yyActionEntry(int lookahead, int action, int next){
    this.lookahead = lookahead;
    this.action = action;
    this.next = next;
  }

  public yyActionEntry next(){
    if(next < 0){
      return null;
    }
    return yyActionTable[next];
  }
};
public static yyActionEntry[] yyActionTable = {
/* State 0 */
/* 0 */  new yyActionEntry(  73, 244, -1   ), /*                 UPDATE shift  244 */
/* 1 */  new yyActionEntry(  15, 222, -1   ), /*                 CREATE shift  222 */
/* 2 */  new yyActionEntry(  83,   1, -1   ), /*                cmdlist shift  1 */
/* 3 */  new yyActionEntry(130,0,-1), /* Unused */
/* 4 */  new yyActionEntry(130,0,-1), /* Unused */
/* 5 */  new yyActionEntry(130,0,-1), /* Unused */
/* 6 */  new yyActionEntry(130,0,-1), /* Unused */
/* 7 */  new yyActionEntry(  39, 256, -1   ), /*                 INSERT shift  256 */
/* 8 */  new yyActionEntry(130,0,-1), /* Unused */
/* 9 */  new yyActionEntry( 105, 470, 0    ), /*                  input accept */
/* 10 */  new yyActionEntry(130,0,-1), /* Unused */
/* 11 */  new yyActionEntry(  75, 291, -1   ), /*                 VACUUM shift  291 */
/* 12 */  new yyActionEntry(130,0,-1), /* Unused */
/* 13 */  new yyActionEntry(130,0,-1), /* Unused */
/* 14 */  new yyActionEntry(  14, 284, -1   ), /*                   COPY shift  284 */
/* 15 */  new yyActionEntry( 111,  37, 1    ), /*              oneselect shift  37 */
/* 16 */  new yyActionEntry(130,0,-1), /* Unused */
/* 17 */  new yyActionEntry(  17, 240, -1   ), /*                 DELETE shift  240 */
/* 18 */  new yyActionEntry(  82, 293, -1   ), /*                    cmd shift  293 */
/* 19 */  new yyActionEntry( 115, 239, 2    ), /*                 select shift  239 */
/* 20 */  new yyActionEntry(130,0,-1), /* Unused */
/* 21 */  new yyActionEntry(130,0,-1), /* Unused */
/* 22 */  new yyActionEntry(  22, 234, -1   ), /*                   DROP shift  234 */
/* 23 */  new yyActionEntry(  23, 295, -1   ), /*            END_OF_FILE shift  295 */
/* 24 */  new yyActionEntry(130,0,-1), /* Unused */
/* 25 */  new yyActionEntry(  89,   6, -1   ), /*           create_table shift  6 */
/* 26 */  new yyActionEntry(  26, 294, -1   ), /*                EXPLAIN shift  294 */
/* 27 */  new yyActionEntry(130,0,-1), /* Unused */
/* 28 */  new yyActionEntry(  92, 304, -1   ), /*                   ecmd shift  304 */
/* 29 */  new yyActionEntry(  61,  41, -1   ), /*                 SELECT shift  41 */
/* 30 */  new yyActionEntry(  94,   4, -1   ), /*                explain shift  4 */
/* 31 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 1 */
/* 32 */  new yyActionEntry(  62,   2, 33   ), /*                   SEMI shift  2 */
/* 33 */  new yyActionEntry(   0, 305, -1   ), /*                      $ reduce 0 */
/* State 2 */
/* 34 */  new yyActionEntry(  73, 244, -1   ), /*                 UPDATE shift  244 */
/* 35 */  new yyActionEntry(  17, 240, -1   ), /*                 DELETE shift  240 */
/* 36 */  new yyActionEntry(  82, 293, -1   ), /*                    cmd shift  293 */
/* 37 */  new yyActionEntry( 115, 239, -1   ), /*                 select shift  239 */
/* 38 */  new yyActionEntry(  14, 284, -1   ), /*                   COPY shift  284 */
/* 39 */  new yyActionEntry(  15, 222, -1   ), /*                 CREATE shift  222 */
/* 40 */  new yyActionEntry(  22, 234, -1   ), /*                   DROP shift  234 */
/* 41 */  new yyActionEntry(  39, 256, -1   ), /*                 INSERT shift  256 */
/* 42 */  new yyActionEntry(130,0,-1), /* Unused */
/* 43 */  new yyActionEntry(  89,   6, 34   ), /*           create_table shift  6 */
/* 44 */  new yyActionEntry(  26, 294, -1   ), /*                EXPLAIN shift  294 */
/* 45 */  new yyActionEntry(  75, 291, -1   ), /*                 VACUUM shift  291 */
/* 46 */  new yyActionEntry(  92,   3, -1   ), /*                   ecmd shift  3 */
/* 47 */  new yyActionEntry(  61,  41, -1   ), /*                 SELECT shift  41 */
/* 48 */  new yyActionEntry(  94,   4, 38   ), /*                explain shift  4 */
/* 49 */  new yyActionEntry( 111,  37, 39   ), /*              oneselect shift  37 */
/* State 3 */
/* 50 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 4 */
/* 51 */  new yyActionEntry(  73, 244, -1   ), /*                 UPDATE shift  244 */
/* 52 */  new yyActionEntry(  17, 240, -1   ), /*                 DELETE shift  240 */
/* 53 */  new yyActionEntry(  82,   5, -1   ), /*                    cmd shift  5 */
/* 54 */  new yyActionEntry( 115, 239, -1   ), /*                 select shift  239 */
/* 55 */  new yyActionEntry(  15, 222, -1   ), /*                 CREATE shift  222 */
/* 56 */  new yyActionEntry(130,0,-1), /* Unused */
/* 57 */  new yyActionEntry(  22, 234, -1   ), /*                   DROP shift  234 */
/* 58 */  new yyActionEntry(  39, 256, -1   ), /*                 INSERT shift  256 */
/* 59 */  new yyActionEntry(130,0,-1), /* Unused */
/* 60 */  new yyActionEntry(  89,   6, 51   ), /*           create_table shift  6 */
/* 61 */  new yyActionEntry(130,0,-1), /* Unused */
/* 62 */  new yyActionEntry(  75, 291, -1   ), /*                 VACUUM shift  291 */
/* 63 */  new yyActionEntry(130,0,-1), /* Unused */
/* 64 */  new yyActionEntry(  61,  41, -1   ), /*                 SELECT shift  41 */
/* 65 */  new yyActionEntry(  14, 284, -1   ), /*                   COPY shift  284 */
/* 66 */  new yyActionEntry( 111,  37, 55   ), /*              oneselect shift  37 */
/* State 5 */
/* 67 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 6 */
/* 68 */  new yyActionEntry(  90,   7, 69   ), /*      create_table_args shift  7 */
/* 69 */  new yyActionEntry(  48,   8, -1   ), /*                     LP shift  8 */
/* State 7 */
/* 70 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 8 */
/* 71 */  new yyActionEntry(  35,  19, -1   ), /*                     ID shift  19 */
/* 72 */  new yyActionEntry(  85,  14, -1   ), /*               columnid shift  14 */
/* 73 */  new yyActionEntry(130,0,-1), /* Unused */
/* 74 */  new yyActionEntry(  67,  20, 71   ), /*                 STRING shift  20 */
/* 75 */  new yyActionEntry(  84, 221, -1   ), /*                 column shift  221 */
/* 76 */  new yyActionEntry( 101, 194, 72   ), /*                     id shift  194 */
/* 77 */  new yyActionEntry(  86,   9, -1   ), /*             columnlist shift  9 */
/* 78 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 9 */
/* 79 */  new yyActionEntry(  88,  10, 80   ), /*           conslist_opt shift  10 */
/* 80 */  new yyActionEntry(  60, 347, -1   ), /*                     RP reduce 42 */
/* 81 */  new yyActionEntry(  10,  12, -1   ), /*                  COMMA shift  12 */
/* 82 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 10 */
/* 83 */  new yyActionEntry(  60,  11, -1   ), /*                     RP shift  11 */
/* State 11 */
/* 84 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 12 */
/* 85 */  new yyActionEntry(  35,  19, -1   ), /*                     ID shift  19 */
/* 86 */  new yyActionEntry(  85,  14, -1   ), /*               columnid shift  14 */
/* 87 */  new yyActionEntry(   8, 217, -1   ), /*                  CHECK shift  217 */
/* 88 */  new yyActionEntry(  67,  20, 85   ), /*                 STRING shift  20 */
/* 89 */  new yyActionEntry(  84,  13, -1   ), /*                 column shift  13 */
/* 90 */  new yyActionEntry( 101, 194, 86   ), /*                     id shift  194 */
/* 91 */  new yyActionEntry(  59, 201, -1   ), /*                PRIMARY shift  201 */
/* 92 */  new yyActionEntry(  87, 195, -1   ), /*               conslist shift  195 */
/* 93 */  new yyActionEntry(  72, 210, 87   ), /*                 UNIQUE shift  210 */
/* 94 */  new yyActionEntry(130,0,-1), /* Unused */
/* 95 */  new yyActionEntry(130,0,-1), /* Unused */
/* 96 */  new yyActionEntry( 123, 220, 91   ), /*                  tcons shift  220 */
/* 97 */  new yyActionEntry( 124, 219, -1   ), /*                 tcons2 shift  219 */
/* 98 */  new yyActionEntry(  13, 198, -1   ), /*             CONSTRAINT shift  198 */
/* 99 */  new yyActionEntry(130,0,-1), /* Unused */
/* 100 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 13 */
/* 101 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 14 */
/* 102 */  new yyActionEntry(  35,  19, -1   ), /*                     ID shift  19 */
/* 103 */  new yyActionEntry( 101, 193, -1   ), /*                     id shift  193 */
/* 104 */  new yyActionEntry(130,0,-1), /* Unused */
/* 105 */  new yyActionEntry(  67,  20, 102  ), /*                 STRING shift  20 */
/* 106 */  new yyActionEntry(130,0,-1), /* Unused */
/* 107 */  new yyActionEntry( 125,  15, 103  ), /*                   type shift  15 */
/* 108 */  new yyActionEntry( 126, 180, -1   ), /*               typename shift  180 */
/* 109 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 15 */
/* 110 */  new yyActionEntry(  80,  16, -1   ), /*               carglist shift  16 */
/* State 16 */
/* 111 */  new yyActionEntry(  72,  30, 113  ), /*                 UNIQUE shift  30 */
/* 112 */  new yyActionEntry(  81, 167, -1   ), /*                  ccons shift  167 */
/* 113 */  new yyActionEntry(  16, 168, 117  ), /*                DEFAULT shift  168 */
/* 114 */  new yyActionEntry(  59,  25, -1   ), /*                PRIMARY shift  25 */
/* 115 */  new yyActionEntry(  52,  23, -1   ), /*                    NOT shift  23 */
/* 116 */  new yyActionEntry(  13,  18, -1   ), /*             CONSTRAINT shift  18 */
/* 117 */  new yyActionEntry(   8,  31, -1   ), /*                  CHECK shift  31 */
/* 118 */  new yyActionEntry(  79,  17, -1   ), /*                   carg shift  17 */
/* State 17 */
/* 119 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 18 */
/* 120 */  new yyActionEntry(  35,  19, -1   ), /*                     ID shift  19 */
/* 121 */  new yyActionEntry( 101,  21, -1   ), /*                     id shift  21 */
/* 122 */  new yyActionEntry(130,0,-1), /* Unused */
/* 123 */  new yyActionEntry(  67,  20, 120  ), /*                 STRING shift  20 */
/* State 19 */
/* 124 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 20 */
/* 125 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 21 */
/* 126 */  new yyActionEntry(  72,  30, 128  ), /*                 UNIQUE shift  30 */
/* 127 */  new yyActionEntry(  81,  22, -1   ), /*                  ccons shift  22 */
/* 128 */  new yyActionEntry(   8,  31, -1   ), /*                  CHECK shift  31 */
/* 129 */  new yyActionEntry(  59,  25, -1   ), /*                PRIMARY shift  25 */
/* 130 */  new yyActionEntry(  52,  23, -1   ), /*                    NOT shift  23 */
/* 131 */  new yyActionEntry(130,0,-1), /* Unused */
/* 132 */  new yyActionEntry(130,0,-1), /* Unused */
/* 133 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 22 */
/* 134 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 23 */
/* 135 */  new yyActionEntry(  54,  24, -1   ), /*                   NULL shift  24 */
/* State 24 */
/* 136 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 25 */
/* 137 */  new yyActionEntry(  45,  26, -1   ), /*                    KEY shift  26 */
/* State 26 */
/* 138 */  new yyActionEntry(   5,  28, -1   ), /*                    ASC shift  28 */
/* 139 */  new yyActionEntry( 121,  27, 138  ), /*              sortorder shift  27 */
/* 140 */  new yyActionEntry(130,0,-1), /* Unused */
/* 141 */  new yyActionEntry(  19,  29, -1   ), /*                   DESC shift  29 */
/* State 27 */
/* 142 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 28 */
/* 143 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 29 */
/* 144 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 30 */
/* 145 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 31 */
/* 146 */  new yyActionEntry(  48,  32, -1   ), /*                     LP shift  32 */
/* State 32 */
/* 147 */  new yyActionEntry(  48,  36, -1   ), /*                     LP shift  36 */
/* 148 */  new yyActionEntry(  35,  33, -1   ), /*                     ID shift  33 */
/* 149 */  new yyActionEntry(  50, 112, -1   ), /*                  MINUS shift  112 */
/* 150 */  new yyActionEntry(  67,  35, 148  ), /*                 STRING shift  35 */
/* 151 */  new yyActionEntry(  52, 110, -1   ), /*                    NOT shift  110 */
/* 152 */  new yyActionEntry( 101,  58, -1   ), /*                     id shift  58 */
/* 153 */  new yyActionEntry(  54,  57, -1   ), /*                   NULL shift  57 */
/* 154 */  new yyActionEntry(130,0,-1), /* Unused */
/* 155 */  new yyActionEntry(  40,  61, -1   ), /*                INTEGER shift  61 */
/* 156 */  new yyActionEntry(130,0,-1), /* Unused */
/* 157 */  new yyActionEntry(  58, 114, -1   ), /*                   PLUS shift  114 */
/* 158 */  new yyActionEntry(  27,  62, -1   ), /*                  FLOAT shift  62 */
/* 159 */  new yyActionEntry(130,0,-1), /* Unused */
/* 160 */  new yyActionEntry(130,0,-1), /* Unused */
/* 161 */  new yyActionEntry(130,0,-1), /* Unused */
/* 162 */  new yyActionEntry(  95, 165, -1   ), /*                   expr shift  165 */
/* State 33 */
/* 163 */  new yyActionEntry(  64, 417, 164  ), /*                  SLASH reduce 112 */
/* 164 */  new yyActionEntry(   0, 417, -1   ), /*                      $ reduce 112 */
/* 165 */  new yyActionEntry(  66, 417, -1   ), /*                   STAR reduce 112 */
/* 166 */  new yyActionEntry(  67, 417, 171  ), /*                 STRING reduce 112 */
/* 167 */  new yyActionEntry(   4, 417, -1   ), /*                     AS reduce 112 */
/* 168 */  new yyActionEntry(   5, 417, -1   ), /*                    ASC reduce 112 */
/* 169 */  new yyActionEntry(   6, 417, -1   ), /*                BETWEEN reduce 112 */
/* 170 */  new yyActionEntry(  71, 417, -1   ), /*                  UNION reduce 112 */
/* 171 */  new yyActionEntry(   3, 417, -1   ), /*                    AND reduce 112 */
/* 172 */  new yyActionEntry(130,0,-1), /* Unused */
/* 173 */  new yyActionEntry(  10, 417, -1   ), /*                  COMMA reduce 112 */
/* 174 */  new yyActionEntry(130,0,-1), /* Unused */
/* 175 */  new yyActionEntry(  12, 417, -1   ), /*                 CONCAT reduce 112 */
/* 176 */  new yyActionEntry(  77, 417, -1   ), /*                  WHERE reduce 112 */
/* 177 */  new yyActionEntry(130,0,-1), /* Unused */
/* 178 */  new yyActionEntry(130,0,-1), /* Unused */
/* 179 */  new yyActionEntry(130,0,-1), /* Unused */
/* 180 */  new yyActionEntry(130,0,-1), /* Unused */
/* 181 */  new yyActionEntry(130,0,-1), /* Unused */
/* 182 */  new yyActionEntry(  19, 417, -1   ), /*                   DESC reduce 112 */
/* 183 */  new yyActionEntry(130,0,-1), /* Unused */
/* 184 */  new yyActionEntry(  21, 320, -1   ), /*                    DOT reduce 15 */
/* 185 */  new yyActionEntry(130,0,-1), /* Unused */
/* 186 */  new yyActionEntry(130,0,-1), /* Unused */
/* 187 */  new yyActionEntry(  24, 417, -1   ), /*                     EQ reduce 112 */
/* 188 */  new yyActionEntry(  25, 417, -1   ), /*                 EXCEPT reduce 112 */
/* 189 */  new yyActionEntry(130,0,-1), /* Unused */
/* 190 */  new yyActionEntry(130,0,-1), /* Unused */
/* 191 */  new yyActionEntry(  28, 417, -1   ), /*                   FROM reduce 112 */
/* 192 */  new yyActionEntry(130,0,-1), /* Unused */
/* 193 */  new yyActionEntry(  30, 417, -1   ), /*                     GE reduce 112 */
/* 194 */  new yyActionEntry(  31, 417, -1   ), /*                   GLOB reduce 112 */
/* 195 */  new yyActionEntry(  32, 417, -1   ), /*                  GROUP reduce 112 */
/* 196 */  new yyActionEntry(  33, 417, -1   ), /*                     GT reduce 112 */
/* 197 */  new yyActionEntry(  34, 417, -1   ), /*                 HAVING reduce 112 */
/* 198 */  new yyActionEntry(  35, 417, -1   ), /*                     ID reduce 112 */
/* 199 */  new yyActionEntry(130,0,-1), /* Unused */
/* 200 */  new yyActionEntry(  37, 417, -1   ), /*                     IN reduce 112 */
/* 201 */  new yyActionEntry(130,0,-1), /* Unused */
/* 202 */  new yyActionEntry(130,0,-1), /* Unused */
/* 203 */  new yyActionEntry(130,0,-1), /* Unused */
/* 204 */  new yyActionEntry(  41, 417, -1   ), /*              INTERSECT reduce 112 */
/* 205 */  new yyActionEntry(130,0,-1), /* Unused */
/* 206 */  new yyActionEntry(130,0,-1), /* Unused */
/* 207 */  new yyActionEntry(  44, 417, -1   ), /*                 ISNULL reduce 112 */
/* 208 */  new yyActionEntry(130,0,-1), /* Unused */
/* 209 */  new yyActionEntry(  46, 417, -1   ), /*                     LE reduce 112 */
/* 210 */  new yyActionEntry(  47, 417, -1   ), /*                   LIKE reduce 112 */
/* 211 */  new yyActionEntry(  48,  34, -1   ), /*                     LP shift  34 */
/* 212 */  new yyActionEntry(  49, 417, -1   ), /*                     LT reduce 112 */
/* 213 */  new yyActionEntry(  50, 417, -1   ), /*                  MINUS reduce 112 */
/* 214 */  new yyActionEntry(  51, 417, -1   ), /*                     NE reduce 112 */
/* 215 */  new yyActionEntry(  52, 417, -1   ), /*                    NOT reduce 112 */
/* 216 */  new yyActionEntry(  53, 417, -1   ), /*                NOTNULL reduce 112 */
/* 217 */  new yyActionEntry(130,0,-1), /* Unused */
/* 218 */  new yyActionEntry(130,0,-1), /* Unused */
/* 219 */  new yyActionEntry(  56, 417, -1   ), /*                     OR reduce 112 */
/* 220 */  new yyActionEntry(  57, 417, -1   ), /*                  ORDER reduce 112 */
/* 221 */  new yyActionEntry(  58, 417, -1   ), /*                   PLUS reduce 112 */
/* 222 */  new yyActionEntry(130,0,-1), /* Unused */
/* 223 */  new yyActionEntry(  60, 417, -1   ), /*                     RP reduce 112 */
/* 224 */  new yyActionEntry(130,0,-1), /* Unused */
/* 225 */  new yyActionEntry(  62, 417, -1   ), /*                   SEMI reduce 112 */
/* 226 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 34 */
/* 227 */  new yyActionEntry(  96, 120, 234  ), /*               expritem shift  120 */
/* 228 */  new yyActionEntry(  97, 161, -1   ), /*               exprlist shift  161 */
/* 229 */  new yyActionEntry(  66, 163, 236  ), /*                   STAR shift  163 */
/* 230 */  new yyActionEntry(  67,  35, 239  ), /*                 STRING shift  35 */
/* 231 */  new yyActionEntry(  52, 110, -1   ), /*                    NOT shift  110 */
/* 232 */  new yyActionEntry( 101,  58, -1   ), /*                     id shift  58 */
/* 233 */  new yyActionEntry(  54,  57, -1   ), /*                   NULL shift  57 */
/* 234 */  new yyActionEntry(  48,  36, -1   ), /*                     LP shift  36 */
/* 235 */  new yyActionEntry(  40,  61, -1   ), /*                INTEGER shift  61 */
/* 236 */  new yyActionEntry(  50, 112, -1   ), /*                  MINUS shift  112 */
/* 237 */  new yyActionEntry(  58, 114, -1   ), /*                   PLUS shift  114 */
/* 238 */  new yyActionEntry(  27,  62, -1   ), /*                  FLOAT shift  62 */
/* 239 */  new yyActionEntry(  35,  33, -1   ), /*                     ID shift  33 */
/* 240 */  new yyActionEntry(130,0,-1), /* Unused */
/* 241 */  new yyActionEntry(130,0,-1), /* Unused */
/* 242 */  new yyActionEntry(  95, 109, -1   ), /*                   expr shift  109 */
/* State 35 */
/* 243 */  new yyActionEntry(  64, 422, 244  ), /*                  SLASH reduce 117 */
/* 244 */  new yyActionEntry(   0, 422, -1   ), /*                      $ reduce 117 */
/* 245 */  new yyActionEntry(  66, 422, -1   ), /*                   STAR reduce 117 */
/* 246 */  new yyActionEntry(  67, 422, 251  ), /*                 STRING reduce 117 */
/* 247 */  new yyActionEntry(   4, 422, -1   ), /*                     AS reduce 117 */
/* 248 */  new yyActionEntry(   5, 422, -1   ), /*                    ASC reduce 117 */
/* 249 */  new yyActionEntry(   6, 422, -1   ), /*                BETWEEN reduce 117 */
/* 250 */  new yyActionEntry(  71, 422, -1   ), /*                  UNION reduce 117 */
/* 251 */  new yyActionEntry(   3, 422, -1   ), /*                    AND reduce 117 */
/* 252 */  new yyActionEntry(130,0,-1), /* Unused */
/* 253 */  new yyActionEntry(  10, 422, -1   ), /*                  COMMA reduce 117 */
/* 254 */  new yyActionEntry(130,0,-1), /* Unused */
/* 255 */  new yyActionEntry(  12, 422, -1   ), /*                 CONCAT reduce 117 */
/* 256 */  new yyActionEntry(  77, 422, -1   ), /*                  WHERE reduce 117 */
/* 257 */  new yyActionEntry(130,0,-1), /* Unused */
/* 258 */  new yyActionEntry(130,0,-1), /* Unused */
/* 259 */  new yyActionEntry(130,0,-1), /* Unused */
/* 260 */  new yyActionEntry(130,0,-1), /* Unused */
/* 261 */  new yyActionEntry(130,0,-1), /* Unused */
/* 262 */  new yyActionEntry(  19, 422, -1   ), /*                   DESC reduce 117 */
/* 263 */  new yyActionEntry(130,0,-1), /* Unused */
/* 264 */  new yyActionEntry(  21, 321, -1   ), /*                    DOT reduce 16 */
/* 265 */  new yyActionEntry(130,0,-1), /* Unused */
/* 266 */  new yyActionEntry(130,0,-1), /* Unused */
/* 267 */  new yyActionEntry(  24, 422, -1   ), /*                     EQ reduce 117 */
/* 268 */  new yyActionEntry(  25, 422, -1   ), /*                 EXCEPT reduce 117 */
/* 269 */  new yyActionEntry(130,0,-1), /* Unused */
/* 270 */  new yyActionEntry(130,0,-1), /* Unused */
/* 271 */  new yyActionEntry(  28, 422, -1   ), /*                   FROM reduce 117 */
/* 272 */  new yyActionEntry(130,0,-1), /* Unused */
/* 273 */  new yyActionEntry(  30, 422, -1   ), /*                     GE reduce 117 */
/* 274 */  new yyActionEntry(  31, 422, -1   ), /*                   GLOB reduce 117 */
/* 275 */  new yyActionEntry(  32, 422, -1   ), /*                  GROUP reduce 117 */
/* 276 */  new yyActionEntry(  33, 422, -1   ), /*                     GT reduce 117 */
/* 277 */  new yyActionEntry(  34, 422, -1   ), /*                 HAVING reduce 117 */
/* 278 */  new yyActionEntry(  35, 422, -1   ), /*                     ID reduce 117 */
/* 279 */  new yyActionEntry(130,0,-1), /* Unused */
/* 280 */  new yyActionEntry(  37, 422, -1   ), /*                     IN reduce 117 */
/* 281 */  new yyActionEntry(130,0,-1), /* Unused */
/* 282 */  new yyActionEntry(130,0,-1), /* Unused */
/* 283 */  new yyActionEntry(130,0,-1), /* Unused */
/* 284 */  new yyActionEntry(  41, 422, -1   ), /*              INTERSECT reduce 117 */
/* 285 */  new yyActionEntry(130,0,-1), /* Unused */
/* 286 */  new yyActionEntry(130,0,-1), /* Unused */
/* 287 */  new yyActionEntry(  44, 422, -1   ), /*                 ISNULL reduce 117 */
/* 288 */  new yyActionEntry(130,0,-1), /* Unused */
/* 289 */  new yyActionEntry(  46, 422, -1   ), /*                     LE reduce 117 */
/* 290 */  new yyActionEntry(  47, 422, -1   ), /*                   LIKE reduce 117 */
/* 291 */  new yyActionEntry(130,0,-1), /* Unused */
/* 292 */  new yyActionEntry(  49, 422, -1   ), /*                     LT reduce 117 */
/* 293 */  new yyActionEntry(  50, 422, -1   ), /*                  MINUS reduce 117 */
/* 294 */  new yyActionEntry(  51, 422, -1   ), /*                     NE reduce 117 */
/* 295 */  new yyActionEntry(  52, 422, -1   ), /*                    NOT reduce 117 */
/* 296 */  new yyActionEntry(  53, 422, -1   ), /*                NOTNULL reduce 117 */
/* 297 */  new yyActionEntry(130,0,-1), /* Unused */
/* 298 */  new yyActionEntry(130,0,-1), /* Unused */
/* 299 */  new yyActionEntry(  56, 422, -1   ), /*                     OR reduce 117 */
/* 300 */  new yyActionEntry(  57, 422, -1   ), /*                  ORDER reduce 117 */
/* 301 */  new yyActionEntry(  58, 422, -1   ), /*                   PLUS reduce 117 */
/* 302 */  new yyActionEntry(130,0,-1), /* Unused */
/* 303 */  new yyActionEntry(  60, 422, -1   ), /*                     RP reduce 117 */
/* 304 */  new yyActionEntry(130,0,-1), /* Unused */
/* 305 */  new yyActionEntry(  62, 422, -1   ), /*                   SEMI reduce 117 */
/* 306 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 36 */
/* 307 */  new yyActionEntry(  48,  36, -1   ), /*                     LP shift  36 */
/* 308 */  new yyActionEntry(  67,  35, 314  ), /*                 STRING shift  35 */
/* 309 */  new yyActionEntry(  50, 112, -1   ), /*                  MINUS shift  112 */
/* 310 */  new yyActionEntry( 115,  38, 308  ), /*                 select shift  38 */
/* 311 */  new yyActionEntry(  52, 110, -1   ), /*                    NOT shift  110 */
/* 312 */  new yyActionEntry( 101,  58, -1   ), /*                     id shift  58 */
/* 313 */  new yyActionEntry(  54,  57, -1   ), /*                   NULL shift  57 */
/* 314 */  new yyActionEntry(  35,  33, -1   ), /*                     ID shift  33 */
/* 315 */  new yyActionEntry(  40,  61, -1   ), /*                INTEGER shift  61 */
/* 316 */  new yyActionEntry(  95, 159, -1   ), /*                   expr shift  159 */
/* 317 */  new yyActionEntry(  58, 114, -1   ), /*                   PLUS shift  114 */
/* 318 */  new yyActionEntry(  27,  62, -1   ), /*                  FLOAT shift  62 */
/* 319 */  new yyActionEntry(130,0,-1), /* Unused */
/* 320 */  new yyActionEntry(  61,  41, -1   ), /*                 SELECT shift  41 */
/* 321 */  new yyActionEntry(130,0,-1), /* Unused */
/* 322 */  new yyActionEntry( 111,  37, 316  ), /*              oneselect shift  37 */
/* State 37 */
/* 323 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 38 */
/* 324 */  new yyActionEntry(  25, 107, -1   ), /*                 EXCEPT shift  107 */
/* 325 */  new yyActionEntry(  41, 106, 324  ), /*              INTERSECT shift  106 */
/* 326 */  new yyActionEntry(130,0,-1), /* Unused */
/* 327 */  new yyActionEntry(130,0,-1), /* Unused */
/* 328 */  new yyActionEntry(  60, 158, -1   ), /*                     RP shift  158 */
/* 329 */  new yyActionEntry(130,0,-1), /* Unused */
/* 330 */  new yyActionEntry( 110,  39, -1   ), /*                 joinop shift  39 */
/* 331 */  new yyActionEntry(  71, 104, -1   ), /*                  UNION shift  104 */
/* State 39 */
/* 332 */  new yyActionEntry(  61,  41, -1   ), /*                 SELECT shift  41 */
/* 333 */  new yyActionEntry( 111,  40, 332  ), /*              oneselect shift  40 */
/* State 40 */
/* 334 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 41 */
/* 335 */  new yyActionEntry(  20, 156, -1   ), /*               DISTINCT shift  156 */
/* 336 */  new yyActionEntry(130,0,-1), /* Unused */
/* 337 */  new yyActionEntry(   2, 157, -1   ), /*                    ALL shift  157 */
/* 338 */  new yyActionEntry(  91,  42, -1   ), /*               distinct shift  42 */
/* State 42 */
/* 339 */  new yyActionEntry(  66, 151, -1   ), /*                   STAR shift  151 */
/* 340 */  new yyActionEntry( 113, 152, -1   ), /*                   sclp shift  152 */
/* 341 */  new yyActionEntry( 114,  43, 339  ), /*             selcollist shift  43 */
/* 342 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 43 */
/* 343 */  new yyActionEntry(  28, 143, -1   ), /*                   FROM shift  143 */
/* 344 */  new yyActionEntry(  10, 142, -1   ), /*                  COMMA shift  142 */
/* 345 */  new yyActionEntry(  98,  44, 344  ), /*                   from shift  44 */
/* 346 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 44 */
/* 347 */  new yyActionEntry( 128,  45, -1   ), /*              where_opt shift  45 */
/* 348 */  new yyActionEntry(  77, 140, -1   ), /*                  WHERE shift  140 */
/* State 45 */
/* 349 */  new yyActionEntry(  32, 137, -1   ), /*                  GROUP shift  137 */
/* 350 */  new yyActionEntry(  99,  46, -1   ), /*            groupby_opt shift  46 */
/* State 46 */
/* 351 */  new yyActionEntry( 100,  47, 352  ), /*             having_opt shift  47 */
/* 352 */  new yyActionEntry(  34, 135, -1   ), /*                 HAVING shift  135 */
/* State 47 */
/* 353 */  new yyActionEntry( 112,  48, -1   ), /*            orderby_opt shift  48 */
/* 354 */  new yyActionEntry(  57,  49, -1   ), /*                  ORDER shift  49 */
/* State 48 */
/* 355 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 49 */
/* 356 */  new yyActionEntry(   7,  50, -1   ), /*                     BY shift  50 */
/* State 50 */
/* 357 */  new yyActionEntry(  48,  36, -1   ), /*                     LP shift  36 */
/* 358 */  new yyActionEntry(  35,  33, -1   ), /*                     ID shift  33 */
/* 359 */  new yyActionEntry(  50, 112, -1   ), /*                  MINUS shift  112 */
/* 360 */  new yyActionEntry(  67,  35, 358  ), /*                 STRING shift  35 */
/* 361 */  new yyActionEntry(  52, 110, -1   ), /*                    NOT shift  110 */
/* 362 */  new yyActionEntry( 101,  58, -1   ), /*                     id shift  58 */
/* 363 */  new yyActionEntry(  54,  57, -1   ), /*                   NULL shift  57 */
/* 364 */  new yyActionEntry( 119, 133, -1   ), /*               sortitem shift  133 */
/* 365 */  new yyActionEntry( 120,  51, 366  ), /*               sortlist shift  51 */
/* 366 */  new yyActionEntry(  40,  61, -1   ), /*                INTEGER shift  61 */
/* 367 */  new yyActionEntry(  58, 114, -1   ), /*                   PLUS shift  114 */
/* 368 */  new yyActionEntry(  27,  62, -1   ), /*                  FLOAT shift  62 */
/* 369 */  new yyActionEntry(130,0,-1), /* Unused */
/* 370 */  new yyActionEntry(130,0,-1), /* Unused */
/* 371 */  new yyActionEntry(130,0,-1), /* Unused */
/* 372 */  new yyActionEntry(  95,  55, -1   ), /*                   expr shift  55 */
/* State 51 */
/* 373 */  new yyActionEntry(  10,  52, -1   ), /*                  COMMA shift  52 */
/* State 52 */
/* 374 */  new yyActionEntry(  48,  36, -1   ), /*                     LP shift  36 */
/* 375 */  new yyActionEntry(  35,  33, -1   ), /*                     ID shift  33 */
/* 376 */  new yyActionEntry(  50, 112, -1   ), /*                  MINUS shift  112 */
/* 377 */  new yyActionEntry(  67,  35, 375  ), /*                 STRING shift  35 */
/* 378 */  new yyActionEntry(  52, 110, -1   ), /*                    NOT shift  110 */
/* 379 */  new yyActionEntry( 101,  58, -1   ), /*                     id shift  58 */
/* 380 */  new yyActionEntry(  54,  57, -1   ), /*                   NULL shift  57 */
/* 381 */  new yyActionEntry( 119,  53, -1   ), /*               sortitem shift  53 */
/* 382 */  new yyActionEntry(  40,  61, -1   ), /*                INTEGER shift  61 */
/* 383 */  new yyActionEntry(130,0,-1), /* Unused */
/* 384 */  new yyActionEntry(  58, 114, -1   ), /*                   PLUS shift  114 */
/* 385 */  new yyActionEntry(  27,  62, -1   ), /*                  FLOAT shift  62 */
/* 386 */  new yyActionEntry(130,0,-1), /* Unused */
/* 387 */  new yyActionEntry(130,0,-1), /* Unused */
/* 388 */  new yyActionEntry(130,0,-1), /* Unused */
/* 389 */  new yyActionEntry(  95,  55, -1   ), /*                   expr shift  55 */
/* State 53 */
/* 390 */  new yyActionEntry(   5,  28, -1   ), /*                    ASC shift  28 */
/* 391 */  new yyActionEntry( 121,  54, 390  ), /*              sortorder shift  54 */
/* 392 */  new yyActionEntry(130,0,-1), /* Unused */
/* 393 */  new yyActionEntry(  19,  29, -1   ), /*                   DESC shift  29 */
/* State 54 */
/* 394 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 55 */
/* 395 */  new yyActionEntry(  64,  91, -1   ), /*                  SLASH shift  91 */
/* 396 */  new yyActionEntry(  33,  68, -1   ), /*                     GT shift  68 */
/* 397 */  new yyActionEntry(  66,  89, -1   ), /*                   STAR shift  89 */
/* 398 */  new yyActionEntry(   3,  56, -1   ), /*                    AND shift  56 */
/* 399 */  new yyActionEntry(  12,  93, -1   ), /*                 CONCAT shift  93 */
/* 400 */  new yyActionEntry(  37, 101, -1   ), /*                     IN shift  101 */
/* 401 */  new yyActionEntry(   6,  97, -1   ), /*                BETWEEN shift  97 */
/* 402 */  new yyActionEntry(  24,  76, -1   ), /*                     EQ shift  76 */
/* 403 */  new yyActionEntry(130,0,-1), /* Unused */
/* 404 */  new yyActionEntry(130,0,-1), /* Unused */
/* 405 */  new yyActionEntry(130,0,-1), /* Unused */
/* 406 */  new yyActionEntry(130,0,-1), /* Unused */
/* 407 */  new yyActionEntry(  44,  95, 399  ), /*                 ISNULL shift  95 */
/* 408 */  new yyActionEntry(130,0,-1), /* Unused */
/* 409 */  new yyActionEntry(  46,  70, -1   ), /*                     LE shift  70 */
/* 410 */  new yyActionEntry(  47,  78, -1   ), /*                   LIKE shift  78 */
/* 411 */  new yyActionEntry(130,0,-1), /* Unused */
/* 412 */  new yyActionEntry(  49,  66, -1   ), /*                     LT shift  66 */
/* 413 */  new yyActionEntry(  50,  87, -1   ), /*                  MINUS shift  87 */
/* 414 */  new yyActionEntry(  51,  74, -1   ), /*                     NE shift  74 */
/* 415 */  new yyActionEntry(  52,  80, -1   ), /*                    NOT shift  80 */
/* 416 */  new yyActionEntry(  53,  96, -1   ), /*                NOTNULL shift  96 */
/* 417 */  new yyActionEntry(130,0,-1), /* Unused */
/* 418 */  new yyActionEntry(130,0,-1), /* Unused */
/* 419 */  new yyActionEntry(  56,  64, 402  ), /*                     OR shift  64 */
/* 420 */  new yyActionEntry(130,0,-1), /* Unused */
/* 421 */  new yyActionEntry(  58,  85, -1   ), /*                   PLUS shift  85 */
/* 422 */  new yyActionEntry(130,0,-1), /* Unused */
/* 423 */  new yyActionEntry(130,0,-1), /* Unused */
/* 424 */  new yyActionEntry(130,0,-1), /* Unused */
/* 425 */  new yyActionEntry(  30,  72, -1   ), /*                     GE shift  72 */
/* 426 */  new yyActionEntry(  31,  83, -1   ), /*                   GLOB shift  83 */
/* State 56 */
/* 427 */  new yyActionEntry(  48,  36, -1   ), /*                     LP shift  36 */
/* 428 */  new yyActionEntry(  35,  33, -1   ), /*                     ID shift  33 */
/* 429 */  new yyActionEntry(  50, 112, -1   ), /*                  MINUS shift  112 */
/* 430 */  new yyActionEntry(  67,  35, 428  ), /*                 STRING shift  35 */
/* 431 */  new yyActionEntry(  52, 110, -1   ), /*                    NOT shift  110 */
/* 432 */  new yyActionEntry( 101,  58, -1   ), /*                     id shift  58 */
/* 433 */  new yyActionEntry(  54,  57, -1   ), /*                   NULL shift  57 */
/* 434 */  new yyActionEntry(130,0,-1), /* Unused */
/* 435 */  new yyActionEntry(  40,  61, -1   ), /*                INTEGER shift  61 */
/* 436 */  new yyActionEntry(130,0,-1), /* Unused */
/* 437 */  new yyActionEntry(  58, 114, -1   ), /*                   PLUS shift  114 */
/* 438 */  new yyActionEntry(  27,  62, -1   ), /*                  FLOAT shift  62 */
/* 439 */  new yyActionEntry(130,0,-1), /* Unused */
/* 440 */  new yyActionEntry(130,0,-1), /* Unused */
/* 441 */  new yyActionEntry(130,0,-1), /* Unused */
/* 442 */  new yyActionEntry(  95,  63, -1   ), /*                   expr shift  63 */
/* State 57 */
/* 443 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 58 */
/* 444 */  new yyActionEntry(  21,  59, -1   ), /*                    DOT shift  59 */
/* State 59 */
/* 445 */  new yyActionEntry(  35,  19, -1   ), /*                     ID shift  19 */
/* 446 */  new yyActionEntry( 101,  60, -1   ), /*                     id shift  60 */
/* 447 */  new yyActionEntry(130,0,-1), /* Unused */
/* 448 */  new yyActionEntry(  67,  20, 445  ), /*                 STRING shift  20 */
/* State 60 */
/* 449 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 61 */
/* 450 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 62 */
/* 451 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 63 */
/* 452 */  new yyActionEntry(  64,  91, -1   ), /*                  SLASH shift  91 */
/* 453 */  new yyActionEntry(  33,  68, -1   ), /*                     GT shift  68 */
/* 454 */  new yyActionEntry(  66,  89, -1   ), /*                   STAR shift  89 */
/* 455 */  new yyActionEntry(  12,  93, -1   ), /*                 CONCAT shift  93 */
/* 456 */  new yyActionEntry(130,0,-1), /* Unused */
/* 457 */  new yyActionEntry(  37, 101, -1   ), /*                     IN shift  101 */
/* 458 */  new yyActionEntry(   6,  97, -1   ), /*                BETWEEN shift  97 */
/* 459 */  new yyActionEntry(130,0,-1), /* Unused */
/* 460 */  new yyActionEntry(130,0,-1), /* Unused */
/* 461 */  new yyActionEntry(130,0,-1), /* Unused */
/* 462 */  new yyActionEntry(130,0,-1), /* Unused */
/* 463 */  new yyActionEntry(130,0,-1), /* Unused */
/* 464 */  new yyActionEntry(  44,  95, 455  ), /*                 ISNULL shift  95 */
/* 465 */  new yyActionEntry(130,0,-1), /* Unused */
/* 466 */  new yyActionEntry(  46,  70, -1   ), /*                     LE shift  70 */
/* 467 */  new yyActionEntry(  47,  78, -1   ), /*                   LIKE shift  78 */
/* 468 */  new yyActionEntry(130,0,-1), /* Unused */
/* 469 */  new yyActionEntry(  49,  66, -1   ), /*                     LT shift  66 */
/* 470 */  new yyActionEntry(  50,  87, -1   ), /*                  MINUS shift  87 */
/* 471 */  new yyActionEntry(  51,  74, -1   ), /*                     NE shift  74 */
/* 472 */  new yyActionEntry(  52,  80, -1   ), /*                    NOT shift  80 */
/* 473 */  new yyActionEntry(  53,  96, -1   ), /*                NOTNULL shift  96 */
/* 474 */  new yyActionEntry(130,0,-1), /* Unused */
/* 475 */  new yyActionEntry(130,0,-1), /* Unused */
/* 476 */  new yyActionEntry(  24,  76, -1   ), /*                     EQ shift  76 */
/* 477 */  new yyActionEntry(130,0,-1), /* Unused */
/* 478 */  new yyActionEntry(  58,  85, -1   ), /*                   PLUS shift  85 */
/* 479 */  new yyActionEntry(130,0,-1), /* Unused */
/* 480 */  new yyActionEntry(130,0,-1), /* Unused */
/* 481 */  new yyActionEntry(130,0,-1), /* Unused */
/* 482 */  new yyActionEntry(  30,  72, -1   ), /*                     GE shift  72 */
/* 483 */  new yyActionEntry(  31,  83, -1   ), /*                   GLOB shift  83 */
/* State 64 */
/* 484 */  new yyActionEntry(  48,  36, -1   ), /*                     LP shift  36 */
/* 485 */  new yyActionEntry(  35,  33, -1   ), /*                     ID shift  33 */
/* 486 */  new yyActionEntry(  50, 112, -1   ), /*                  MINUS shift  112 */
/* 487 */  new yyActionEntry(  67,  35, 485  ), /*                 STRING shift  35 */
/* 488 */  new yyActionEntry(  52, 110, -1   ), /*                    NOT shift  110 */
/* 489 */  new yyActionEntry( 101,  58, -1   ), /*                     id shift  58 */
/* 490 */  new yyActionEntry(  54,  57, -1   ), /*                   NULL shift  57 */
/* 491 */  new yyActionEntry(130,0,-1), /* Unused */
/* 492 */  new yyActionEntry(  40,  61, -1   ), /*                INTEGER shift  61 */
/* 493 */  new yyActionEntry(130,0,-1), /* Unused */
/* 494 */  new yyActionEntry(  58, 114, -1   ), /*                   PLUS shift  114 */
/* 495 */  new yyActionEntry(  27,  62, -1   ), /*                  FLOAT shift  62 */
/* 496 */  new yyActionEntry(130,0,-1), /* Unused */
/* 497 */  new yyActionEntry(130,0,-1), /* Unused */
/* 498 */  new yyActionEntry(130,0,-1), /* Unused */
/* 499 */  new yyActionEntry(  95,  65, -1   ), /*                   expr shift  65 */
/* State 65 */
/* 500 */  new yyActionEntry(  64,  91, -1   ), /*                  SLASH shift  91 */
/* 501 */  new yyActionEntry(  33,  68, -1   ), /*                     GT shift  68 */
/* 502 */  new yyActionEntry(  66,  89, -1   ), /*                   STAR shift  89 */
/* 503 */  new yyActionEntry(   3,  56, -1   ), /*                    AND shift  56 */
/* 504 */  new yyActionEntry(  12,  93, -1   ), /*                 CONCAT shift  93 */
/* 505 */  new yyActionEntry(  37, 101, -1   ), /*                     IN shift  101 */
/* 506 */  new yyActionEntry(   6,  97, -1   ), /*                BETWEEN shift  97 */
/* 507 */  new yyActionEntry(130,0,-1), /* Unused */
/* 508 */  new yyActionEntry(130,0,-1), /* Unused */
/* 509 */  new yyActionEntry(130,0,-1), /* Unused */
/* 510 */  new yyActionEntry(130,0,-1), /* Unused */
/* 511 */  new yyActionEntry(130,0,-1), /* Unused */
/* 512 */  new yyActionEntry(  44,  95, 504  ), /*                 ISNULL shift  95 */
/* 513 */  new yyActionEntry(130,0,-1), /* Unused */
/* 514 */  new yyActionEntry(  46,  70, -1   ), /*                     LE shift  70 */
/* 515 */  new yyActionEntry(  47,  78, -1   ), /*                   LIKE shift  78 */
/* 516 */  new yyActionEntry(130,0,-1), /* Unused */
/* 517 */  new yyActionEntry(  49,  66, -1   ), /*                     LT shift  66 */
/* 518 */  new yyActionEntry(  50,  87, -1   ), /*                  MINUS shift  87 */
/* 519 */  new yyActionEntry(  51,  74, -1   ), /*                     NE shift  74 */
/* 520 */  new yyActionEntry(  52,  80, -1   ), /*                    NOT shift  80 */
/* 521 */  new yyActionEntry(  53,  96, -1   ), /*                NOTNULL shift  96 */
/* 522 */  new yyActionEntry(130,0,-1), /* Unused */
/* 523 */  new yyActionEntry(130,0,-1), /* Unused */
/* 524 */  new yyActionEntry(  24,  76, -1   ), /*                     EQ shift  76 */
/* 525 */  new yyActionEntry(130,0,-1), /* Unused */
/* 526 */  new yyActionEntry(  58,  85, -1   ), /*                   PLUS shift  85 */
/* 527 */  new yyActionEntry(130,0,-1), /* Unused */
/* 528 */  new yyActionEntry(130,0,-1), /* Unused */
/* 529 */  new yyActionEntry(130,0,-1), /* Unused */
/* 530 */  new yyActionEntry(  30,  72, -1   ), /*                     GE shift  72 */
/* 531 */  new yyActionEntry(  31,  83, -1   ), /*                   GLOB shift  83 */
/* State 66 */
/* 532 */  new yyActionEntry(  48,  36, -1   ), /*                     LP shift  36 */
/* 533 */  new yyActionEntry(  35,  33, -1   ), /*                     ID shift  33 */
/* 534 */  new yyActionEntry(  50, 112, -1   ), /*                  MINUS shift  112 */
/* 535 */  new yyActionEntry(  67,  35, 533  ), /*                 STRING shift  35 */
/* 536 */  new yyActionEntry(  52, 110, -1   ), /*                    NOT shift  110 */
/* 537 */  new yyActionEntry( 101,  58, -1   ), /*                     id shift  58 */
/* 538 */  new yyActionEntry(  54,  57, -1   ), /*                   NULL shift  57 */
/* 539 */  new yyActionEntry(130,0,-1), /* Unused */
/* 540 */  new yyActionEntry(  40,  61, -1   ), /*                INTEGER shift  61 */
/* 541 */  new yyActionEntry(130,0,-1), /* Unused */
/* 542 */  new yyActionEntry(  58, 114, -1   ), /*                   PLUS shift  114 */
/* 543 */  new yyActionEntry(  27,  62, -1   ), /*                  FLOAT shift  62 */
/* 544 */  new yyActionEntry(130,0,-1), /* Unused */
/* 545 */  new yyActionEntry(130,0,-1), /* Unused */
/* 546 */  new yyActionEntry(130,0,-1), /* Unused */
/* 547 */  new yyActionEntry(  95,  67, -1   ), /*                   expr shift  67 */
/* State 67 */
/* 548 */  new yyActionEntry(  64,  91, -1   ), /*                  SLASH shift  91 */
/* 549 */  new yyActionEntry(  58,  85, 551  ), /*                   PLUS shift  85 */
/* 550 */  new yyActionEntry(  66,  89, 549  ), /*                   STAR shift  89 */
/* 551 */  new yyActionEntry(  50,  87, -1   ), /*                  MINUS shift  87 */
/* 552 */  new yyActionEntry(  12,  93, -1   ), /*                 CONCAT shift  93 */
/* 553 */  new yyActionEntry(130,0,-1), /* Unused */
/* 554 */  new yyActionEntry(130,0,-1), /* Unused */
/* 555 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 68 */
/* 556 */  new yyActionEntry(  48,  36, -1   ), /*                     LP shift  36 */
/* 557 */  new yyActionEntry(  35,  33, -1   ), /*                     ID shift  33 */
/* 558 */  new yyActionEntry(  50, 112, -1   ), /*                  MINUS shift  112 */
/* 559 */  new yyActionEntry(  67,  35, 557  ), /*                 STRING shift  35 */
/* 560 */  new yyActionEntry(  52, 110, -1   ), /*                    NOT shift  110 */
/* 561 */  new yyActionEntry( 101,  58, -1   ), /*                     id shift  58 */
/* 562 */  new yyActionEntry(  54,  57, -1   ), /*                   NULL shift  57 */
/* 563 */  new yyActionEntry(130,0,-1), /* Unused */
/* 564 */  new yyActionEntry(  40,  61, -1   ), /*                INTEGER shift  61 */
/* 565 */  new yyActionEntry(130,0,-1), /* Unused */
/* 566 */  new yyActionEntry(  58, 114, -1   ), /*                   PLUS shift  114 */
/* 567 */  new yyActionEntry(  27,  62, -1   ), /*                  FLOAT shift  62 */
/* 568 */  new yyActionEntry(130,0,-1), /* Unused */
/* 569 */  new yyActionEntry(130,0,-1), /* Unused */
/* 570 */  new yyActionEntry(130,0,-1), /* Unused */
/* 571 */  new yyActionEntry(  95,  69, -1   ), /*                   expr shift  69 */
/* State 69 */
/* 572 */  new yyActionEntry(  64,  91, -1   ), /*                  SLASH shift  91 */
/* 573 */  new yyActionEntry(  58,  85, 575  ), /*                   PLUS shift  85 */
/* 574 */  new yyActionEntry(  66,  89, 573  ), /*                   STAR shift  89 */
/* 575 */  new yyActionEntry(  50,  87, -1   ), /*                  MINUS shift  87 */
/* 576 */  new yyActionEntry(  12,  93, -1   ), /*                 CONCAT shift  93 */
/* 577 */  new yyActionEntry(130,0,-1), /* Unused */
/* 578 */  new yyActionEntry(130,0,-1), /* Unused */
/* 579 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 70 */
/* 580 */  new yyActionEntry(  48,  36, -1   ), /*                     LP shift  36 */
/* 581 */  new yyActionEntry(  35,  33, -1   ), /*                     ID shift  33 */
/* 582 */  new yyActionEntry(  50, 112, -1   ), /*                  MINUS shift  112 */
/* 583 */  new yyActionEntry(  67,  35, 581  ), /*                 STRING shift  35 */
/* 584 */  new yyActionEntry(  52, 110, -1   ), /*                    NOT shift  110 */
/* 585 */  new yyActionEntry( 101,  58, -1   ), /*                     id shift  58 */
/* 586 */  new yyActionEntry(  54,  57, -1   ), /*                   NULL shift  57 */
/* 587 */  new yyActionEntry(130,0,-1), /* Unused */
/* 588 */  new yyActionEntry(  40,  61, -1   ), /*                INTEGER shift  61 */
/* 589 */  new yyActionEntry(130,0,-1), /* Unused */
/* 590 */  new yyActionEntry(  58, 114, -1   ), /*                   PLUS shift  114 */
/* 591 */  new yyActionEntry(  27,  62, -1   ), /*                  FLOAT shift  62 */
/* 592 */  new yyActionEntry(130,0,-1), /* Unused */
/* 593 */  new yyActionEntry(130,0,-1), /* Unused */
/* 594 */  new yyActionEntry(130,0,-1), /* Unused */
/* 595 */  new yyActionEntry(  95,  71, -1   ), /*                   expr shift  71 */
/* State 71 */
/* 596 */  new yyActionEntry(  64,  91, -1   ), /*                  SLASH shift  91 */
/* 597 */  new yyActionEntry(  58,  85, 599  ), /*                   PLUS shift  85 */
/* 598 */  new yyActionEntry(  66,  89, 597  ), /*                   STAR shift  89 */
/* 599 */  new yyActionEntry(  50,  87, -1   ), /*                  MINUS shift  87 */
/* 600 */  new yyActionEntry(  12,  93, -1   ), /*                 CONCAT shift  93 */
/* 601 */  new yyActionEntry(130,0,-1), /* Unused */
/* 602 */  new yyActionEntry(130,0,-1), /* Unused */
/* 603 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 72 */
/* 604 */  new yyActionEntry(  48,  36, -1   ), /*                     LP shift  36 */
/* 605 */  new yyActionEntry(  35,  33, -1   ), /*                     ID shift  33 */
/* 606 */  new yyActionEntry(  50, 112, -1   ), /*                  MINUS shift  112 */
/* 607 */  new yyActionEntry(  67,  35, 605  ), /*                 STRING shift  35 */
/* 608 */  new yyActionEntry(  52, 110, -1   ), /*                    NOT shift  110 */
/* 609 */  new yyActionEntry( 101,  58, -1   ), /*                     id shift  58 */
/* 610 */  new yyActionEntry(  54,  57, -1   ), /*                   NULL shift  57 */
/* 611 */  new yyActionEntry(130,0,-1), /* Unused */
/* 612 */  new yyActionEntry(  40,  61, -1   ), /*                INTEGER shift  61 */
/* 613 */  new yyActionEntry(130,0,-1), /* Unused */
/* 614 */  new yyActionEntry(  58, 114, -1   ), /*                   PLUS shift  114 */
/* 615 */  new yyActionEntry(  27,  62, -1   ), /*                  FLOAT shift  62 */
/* 616 */  new yyActionEntry(130,0,-1), /* Unused */
/* 617 */  new yyActionEntry(130,0,-1), /* Unused */
/* 618 */  new yyActionEntry(130,0,-1), /* Unused */
/* 619 */  new yyActionEntry(  95,  73, -1   ), /*                   expr shift  73 */
/* State 73 */
/* 620 */  new yyActionEntry(  64,  91, -1   ), /*                  SLASH shift  91 */
/* 621 */  new yyActionEntry(  58,  85, 623  ), /*                   PLUS shift  85 */
/* 622 */  new yyActionEntry(  66,  89, 621  ), /*                   STAR shift  89 */
/* 623 */  new yyActionEntry(  50,  87, -1   ), /*                  MINUS shift  87 */
/* 624 */  new yyActionEntry(  12,  93, -1   ), /*                 CONCAT shift  93 */
/* 625 */  new yyActionEntry(130,0,-1), /* Unused */
/* 626 */  new yyActionEntry(130,0,-1), /* Unused */
/* 627 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 74 */
/* 628 */  new yyActionEntry(  48,  36, -1   ), /*                     LP shift  36 */
/* 629 */  new yyActionEntry(  35,  33, -1   ), /*                     ID shift  33 */
/* 630 */  new yyActionEntry(  50, 112, -1   ), /*                  MINUS shift  112 */
/* 631 */  new yyActionEntry(  67,  35, 629  ), /*                 STRING shift  35 */
/* 632 */  new yyActionEntry(  52, 110, -1   ), /*                    NOT shift  110 */
/* 633 */  new yyActionEntry( 101,  58, -1   ), /*                     id shift  58 */
/* 634 */  new yyActionEntry(  54,  57, -1   ), /*                   NULL shift  57 */
/* 635 */  new yyActionEntry(130,0,-1), /* Unused */
/* 636 */  new yyActionEntry(  40,  61, -1   ), /*                INTEGER shift  61 */
/* 637 */  new yyActionEntry(130,0,-1), /* Unused */
/* 638 */  new yyActionEntry(  58, 114, -1   ), /*                   PLUS shift  114 */
/* 639 */  new yyActionEntry(  27,  62, -1   ), /*                  FLOAT shift  62 */
/* 640 */  new yyActionEntry(130,0,-1), /* Unused */
/* 641 */  new yyActionEntry(130,0,-1), /* Unused */
/* 642 */  new yyActionEntry(130,0,-1), /* Unused */
/* 643 */  new yyActionEntry(  95,  75, -1   ), /*                   expr shift  75 */
/* State 75 */
/* 644 */  new yyActionEntry(  64,  91, -1   ), /*                  SLASH shift  91 */
/* 645 */  new yyActionEntry(  49,  66, 647  ), /*                     LT shift  66 */
/* 646 */  new yyActionEntry(  66,  89, 648  ), /*                   STAR shift  89 */
/* 647 */  new yyActionEntry(  33,  68, -1   ), /*                     GT shift  68 */
/* 648 */  new yyActionEntry(  50,  87, -1   ), /*                  MINUS shift  87 */
/* 649 */  new yyActionEntry(  30,  72, -1   ), /*                     GE shift  72 */
/* 650 */  new yyActionEntry(130,0,-1), /* Unused */
/* 651 */  new yyActionEntry(130,0,-1), /* Unused */
/* 652 */  new yyActionEntry(130,0,-1), /* Unused */
/* 653 */  new yyActionEntry(130,0,-1), /* Unused */
/* 654 */  new yyActionEntry(  58,  85, -1   ), /*                   PLUS shift  85 */
/* 655 */  new yyActionEntry(130,0,-1), /* Unused */
/* 656 */  new yyActionEntry(  12,  93, -1   ), /*                 CONCAT shift  93 */
/* 657 */  new yyActionEntry(130,0,-1), /* Unused */
/* 658 */  new yyActionEntry(  46,  70, 649  ), /*                     LE shift  70 */
/* 659 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 76 */
/* 660 */  new yyActionEntry(  48,  36, -1   ), /*                     LP shift  36 */
/* 661 */  new yyActionEntry(  35,  33, -1   ), /*                     ID shift  33 */
/* 662 */  new yyActionEntry(  50, 112, -1   ), /*                  MINUS shift  112 */
/* 663 */  new yyActionEntry(  67,  35, 661  ), /*                 STRING shift  35 */
/* 664 */  new yyActionEntry(  52, 110, -1   ), /*                    NOT shift  110 */
/* 665 */  new yyActionEntry( 101,  58, -1   ), /*                     id shift  58 */
/* 666 */  new yyActionEntry(  54,  57, -1   ), /*                   NULL shift  57 */
/* 667 */  new yyActionEntry(130,0,-1), /* Unused */
/* 668 */  new yyActionEntry(  40,  61, -1   ), /*                INTEGER shift  61 */
/* 669 */  new yyActionEntry(130,0,-1), /* Unused */
/* 670 */  new yyActionEntry(  58, 114, -1   ), /*                   PLUS shift  114 */
/* 671 */  new yyActionEntry(  27,  62, -1   ), /*                  FLOAT shift  62 */
/* 672 */  new yyActionEntry(130,0,-1), /* Unused */
/* 673 */  new yyActionEntry(130,0,-1), /* Unused */
/* 674 */  new yyActionEntry(130,0,-1), /* Unused */
/* 675 */  new yyActionEntry(  95,  77, -1   ), /*                   expr shift  77 */
/* State 77 */
/* 676 */  new yyActionEntry(  64,  91, -1   ), /*                  SLASH shift  91 */
/* 677 */  new yyActionEntry(  49,  66, 679  ), /*                     LT shift  66 */
/* 678 */  new yyActionEntry(  66,  89, 680  ), /*                   STAR shift  89 */
/* 679 */  new yyActionEntry(  33,  68, -1   ), /*                     GT shift  68 */
/* 680 */  new yyActionEntry(  50,  87, -1   ), /*                  MINUS shift  87 */
/* 681 */  new yyActionEntry(  30,  72, -1   ), /*                     GE shift  72 */
/* 682 */  new yyActionEntry(130,0,-1), /* Unused */
/* 683 */  new yyActionEntry(130,0,-1), /* Unused */
/* 684 */  new yyActionEntry(130,0,-1), /* Unused */
/* 685 */  new yyActionEntry(130,0,-1), /* Unused */
/* 686 */  new yyActionEntry(  58,  85, -1   ), /*                   PLUS shift  85 */
/* 687 */  new yyActionEntry(130,0,-1), /* Unused */
/* 688 */  new yyActionEntry(  12,  93, -1   ), /*                 CONCAT shift  93 */
/* 689 */  new yyActionEntry(130,0,-1), /* Unused */
/* 690 */  new yyActionEntry(  46,  70, 681  ), /*                     LE shift  70 */
/* 691 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 78 */
/* 692 */  new yyActionEntry(  48,  36, -1   ), /*                     LP shift  36 */
/* 693 */  new yyActionEntry(  35,  33, -1   ), /*                     ID shift  33 */
/* 694 */  new yyActionEntry(  50, 112, -1   ), /*                  MINUS shift  112 */
/* 695 */  new yyActionEntry(  67,  35, 693  ), /*                 STRING shift  35 */
/* 696 */  new yyActionEntry(  52, 110, -1   ), /*                    NOT shift  110 */
/* 697 */  new yyActionEntry( 101,  58, -1   ), /*                     id shift  58 */
/* 698 */  new yyActionEntry(  54,  57, -1   ), /*                   NULL shift  57 */
/* 699 */  new yyActionEntry(130,0,-1), /* Unused */
/* 700 */  new yyActionEntry(  40,  61, -1   ), /*                INTEGER shift  61 */
/* 701 */  new yyActionEntry(130,0,-1), /* Unused */
/* 702 */  new yyActionEntry(  58, 114, -1   ), /*                   PLUS shift  114 */
/* 703 */  new yyActionEntry(  27,  62, -1   ), /*                  FLOAT shift  62 */
/* 704 */  new yyActionEntry(130,0,-1), /* Unused */
/* 705 */  new yyActionEntry(130,0,-1), /* Unused */
/* 706 */  new yyActionEntry(130,0,-1), /* Unused */
/* 707 */  new yyActionEntry(  95,  79, -1   ), /*                   expr shift  79 */
/* State 79 */
/* 708 */  new yyActionEntry(  64,  91, -1   ), /*                  SLASH shift  91 */
/* 709 */  new yyActionEntry(  49,  66, 711  ), /*                     LT shift  66 */
/* 710 */  new yyActionEntry(  66,  89, 712  ), /*                   STAR shift  89 */
/* 711 */  new yyActionEntry(  33,  68, -1   ), /*                     GT shift  68 */
/* 712 */  new yyActionEntry(  50,  87, -1   ), /*                  MINUS shift  87 */
/* 713 */  new yyActionEntry(  30,  72, -1   ), /*                     GE shift  72 */
/* 714 */  new yyActionEntry(130,0,-1), /* Unused */
/* 715 */  new yyActionEntry(130,0,-1), /* Unused */
/* 716 */  new yyActionEntry(130,0,-1), /* Unused */
/* 717 */  new yyActionEntry(130,0,-1), /* Unused */
/* 718 */  new yyActionEntry(  58,  85, -1   ), /*                   PLUS shift  85 */
/* 719 */  new yyActionEntry(130,0,-1), /* Unused */
/* 720 */  new yyActionEntry(  12,  93, -1   ), /*                 CONCAT shift  93 */
/* 721 */  new yyActionEntry(130,0,-1), /* Unused */
/* 722 */  new yyActionEntry(  46,  70, 713  ), /*                     LE shift  70 */
/* 723 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 80 */
/* 724 */  new yyActionEntry(  31, 121, -1   ), /*                   GLOB shift  121 */
/* 725 */  new yyActionEntry(  37, 127, -1   ), /*                     IN shift  127 */
/* 726 */  new yyActionEntry(   6, 123, -1   ), /*                BETWEEN shift  123 */
/* 727 */  new yyActionEntry(  47,  81, 724  ), /*                   LIKE shift  81 */
/* State 81 */
/* 728 */  new yyActionEntry(  48,  36, -1   ), /*                     LP shift  36 */
/* 729 */  new yyActionEntry(  35,  33, -1   ), /*                     ID shift  33 */
/* 730 */  new yyActionEntry(  50, 112, -1   ), /*                  MINUS shift  112 */
/* 731 */  new yyActionEntry(  67,  35, 729  ), /*                 STRING shift  35 */
/* 732 */  new yyActionEntry(  52, 110, -1   ), /*                    NOT shift  110 */
/* 733 */  new yyActionEntry( 101,  58, -1   ), /*                     id shift  58 */
/* 734 */  new yyActionEntry(  54,  57, -1   ), /*                   NULL shift  57 */
/* 735 */  new yyActionEntry(130,0,-1), /* Unused */
/* 736 */  new yyActionEntry(  40,  61, -1   ), /*                INTEGER shift  61 */
/* 737 */  new yyActionEntry(130,0,-1), /* Unused */
/* 738 */  new yyActionEntry(  58, 114, -1   ), /*                   PLUS shift  114 */
/* 739 */  new yyActionEntry(  27,  62, -1   ), /*                  FLOAT shift  62 */
/* 740 */  new yyActionEntry(130,0,-1), /* Unused */
/* 741 */  new yyActionEntry(130,0,-1), /* Unused */
/* 742 */  new yyActionEntry(130,0,-1), /* Unused */
/* 743 */  new yyActionEntry(  95,  82, -1   ), /*                   expr shift  82 */
/* State 82 */
/* 744 */  new yyActionEntry(  64,  91, -1   ), /*                  SLASH shift  91 */
/* 745 */  new yyActionEntry(  33,  68, -1   ), /*                     GT shift  68 */
/* 746 */  new yyActionEntry(  66,  89, -1   ), /*                   STAR shift  89 */
/* 747 */  new yyActionEntry(  12,  93, -1   ), /*                 CONCAT shift  93 */
/* 748 */  new yyActionEntry(130,0,-1), /* Unused */
/* 749 */  new yyActionEntry(  37, 101, -1   ), /*                     IN shift  101 */
/* 750 */  new yyActionEntry(   6,  97, -1   ), /*                BETWEEN shift  97 */
/* 751 */  new yyActionEntry(130,0,-1), /* Unused */
/* 752 */  new yyActionEntry(130,0,-1), /* Unused */
/* 753 */  new yyActionEntry(130,0,-1), /* Unused */
/* 754 */  new yyActionEntry(130,0,-1), /* Unused */
/* 755 */  new yyActionEntry(130,0,-1), /* Unused */
/* 756 */  new yyActionEntry(  44,  95, 747  ), /*                 ISNULL shift  95 */
/* 757 */  new yyActionEntry(130,0,-1), /* Unused */
/* 758 */  new yyActionEntry(  46,  70, -1   ), /*                     LE shift  70 */
/* 759 */  new yyActionEntry(  47,  78, -1   ), /*                   LIKE shift  78 */
/* 760 */  new yyActionEntry(130,0,-1), /* Unused */
/* 761 */  new yyActionEntry(  49,  66, -1   ), /*                     LT shift  66 */
/* 762 */  new yyActionEntry(  50,  87, -1   ), /*                  MINUS shift  87 */
/* 763 */  new yyActionEntry(  51,  74, -1   ), /*                     NE shift  74 */
/* 764 */  new yyActionEntry(  52,  80, -1   ), /*                    NOT shift  80 */
/* 765 */  new yyActionEntry(  53,  96, -1   ), /*                NOTNULL shift  96 */
/* 766 */  new yyActionEntry(130,0,-1), /* Unused */
/* 767 */  new yyActionEntry(130,0,-1), /* Unused */
/* 768 */  new yyActionEntry(  24,  76, -1   ), /*                     EQ shift  76 */
/* 769 */  new yyActionEntry(130,0,-1), /* Unused */
/* 770 */  new yyActionEntry(  58,  85, -1   ), /*                   PLUS shift  85 */
/* 771 */  new yyActionEntry(130,0,-1), /* Unused */
/* 772 */  new yyActionEntry(130,0,-1), /* Unused */
/* 773 */  new yyActionEntry(130,0,-1), /* Unused */
/* 774 */  new yyActionEntry(  30,  72, -1   ), /*                     GE shift  72 */
/* 775 */  new yyActionEntry(  31,  83, -1   ), /*                   GLOB shift  83 */
/* State 83 */
/* 776 */  new yyActionEntry(  48,  36, -1   ), /*                     LP shift  36 */
/* 777 */  new yyActionEntry(  35,  33, -1   ), /*                     ID shift  33 */
/* 778 */  new yyActionEntry(  50, 112, -1   ), /*                  MINUS shift  112 */
/* 779 */  new yyActionEntry(  67,  35, 777  ), /*                 STRING shift  35 */
/* 780 */  new yyActionEntry(  52, 110, -1   ), /*                    NOT shift  110 */
/* 781 */  new yyActionEntry( 101,  58, -1   ), /*                     id shift  58 */
/* 782 */  new yyActionEntry(  54,  57, -1   ), /*                   NULL shift  57 */
/* 783 */  new yyActionEntry(130,0,-1), /* Unused */
/* 784 */  new yyActionEntry(  40,  61, -1   ), /*                INTEGER shift  61 */
/* 785 */  new yyActionEntry(130,0,-1), /* Unused */
/* 786 */  new yyActionEntry(  58, 114, -1   ), /*                   PLUS shift  114 */
/* 787 */  new yyActionEntry(  27,  62, -1   ), /*                  FLOAT shift  62 */
/* 788 */  new yyActionEntry(130,0,-1), /* Unused */
/* 789 */  new yyActionEntry(130,0,-1), /* Unused */
/* 790 */  new yyActionEntry(130,0,-1), /* Unused */
/* 791 */  new yyActionEntry(  95,  84, -1   ), /*                   expr shift  84 */
/* State 84 */
/* 792 */  new yyActionEntry(  64,  91, -1   ), /*                  SLASH shift  91 */
/* 793 */  new yyActionEntry(  49,  66, 795  ), /*                     LT shift  66 */
/* 794 */  new yyActionEntry(  66,  89, 796  ), /*                   STAR shift  89 */
/* 795 */  new yyActionEntry(  33,  68, -1   ), /*                     GT shift  68 */
/* 796 */  new yyActionEntry(  50,  87, -1   ), /*                  MINUS shift  87 */
/* 797 */  new yyActionEntry(  30,  72, -1   ), /*                     GE shift  72 */
/* 798 */  new yyActionEntry(130,0,-1), /* Unused */
/* 799 */  new yyActionEntry(130,0,-1), /* Unused */
/* 800 */  new yyActionEntry(130,0,-1), /* Unused */
/* 801 */  new yyActionEntry(130,0,-1), /* Unused */
/* 802 */  new yyActionEntry(  58,  85, -1   ), /*                   PLUS shift  85 */
/* 803 */  new yyActionEntry(130,0,-1), /* Unused */
/* 804 */  new yyActionEntry(  12,  93, -1   ), /*                 CONCAT shift  93 */
/* 805 */  new yyActionEntry(130,0,-1), /* Unused */
/* 806 */  new yyActionEntry(  46,  70, 797  ), /*                     LE shift  70 */
/* 807 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 85 */
/* 808 */  new yyActionEntry(  48,  36, -1   ), /*                     LP shift  36 */
/* 809 */  new yyActionEntry(  35,  33, -1   ), /*                     ID shift  33 */
/* 810 */  new yyActionEntry(  50, 112, -1   ), /*                  MINUS shift  112 */
/* 811 */  new yyActionEntry(  67,  35, 809  ), /*                 STRING shift  35 */
/* 812 */  new yyActionEntry(  52, 110, -1   ), /*                    NOT shift  110 */
/* 813 */  new yyActionEntry( 101,  58, -1   ), /*                     id shift  58 */
/* 814 */  new yyActionEntry(  54,  57, -1   ), /*                   NULL shift  57 */
/* 815 */  new yyActionEntry(130,0,-1), /* Unused */
/* 816 */  new yyActionEntry(  40,  61, -1   ), /*                INTEGER shift  61 */
/* 817 */  new yyActionEntry(130,0,-1), /* Unused */
/* 818 */  new yyActionEntry(  58, 114, -1   ), /*                   PLUS shift  114 */
/* 819 */  new yyActionEntry(  27,  62, -1   ), /*                  FLOAT shift  62 */
/* 820 */  new yyActionEntry(130,0,-1), /* Unused */
/* 821 */  new yyActionEntry(130,0,-1), /* Unused */
/* 822 */  new yyActionEntry(130,0,-1), /* Unused */
/* 823 */  new yyActionEntry(  95,  86, -1   ), /*                   expr shift  86 */
/* State 86 */
/* 824 */  new yyActionEntry(  64,  91, 825  ), /*                  SLASH shift  91 */
/* 825 */  new yyActionEntry(  12,  93, -1   ), /*                 CONCAT shift  93 */
/* 826 */  new yyActionEntry(  66,  89, -1   ), /*                   STAR shift  89 */
/* 827 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 87 */
/* 828 */  new yyActionEntry(  48,  36, -1   ), /*                     LP shift  36 */
/* 829 */  new yyActionEntry(  35,  33, -1   ), /*                     ID shift  33 */
/* 830 */  new yyActionEntry(  50, 112, -1   ), /*                  MINUS shift  112 */
/* 831 */  new yyActionEntry(  67,  35, 829  ), /*                 STRING shift  35 */
/* 832 */  new yyActionEntry(  52, 110, -1   ), /*                    NOT shift  110 */
/* 833 */  new yyActionEntry( 101,  58, -1   ), /*                     id shift  58 */
/* 834 */  new yyActionEntry(  54,  57, -1   ), /*                   NULL shift  57 */
/* 835 */  new yyActionEntry(130,0,-1), /* Unused */
/* 836 */  new yyActionEntry(  40,  61, -1   ), /*                INTEGER shift  61 */
/* 837 */  new yyActionEntry(130,0,-1), /* Unused */
/* 838 */  new yyActionEntry(  58, 114, -1   ), /*                   PLUS shift  114 */
/* 839 */  new yyActionEntry(  27,  62, -1   ), /*                  FLOAT shift  62 */
/* 840 */  new yyActionEntry(130,0,-1), /* Unused */
/* 841 */  new yyActionEntry(130,0,-1), /* Unused */
/* 842 */  new yyActionEntry(130,0,-1), /* Unused */
/* 843 */  new yyActionEntry(  95,  88, -1   ), /*                   expr shift  88 */
/* State 88 */
/* 844 */  new yyActionEntry(  64,  91, 845  ), /*                  SLASH shift  91 */
/* 845 */  new yyActionEntry(  12,  93, -1   ), /*                 CONCAT shift  93 */
/* 846 */  new yyActionEntry(  66,  89, -1   ), /*                   STAR shift  89 */
/* 847 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 89 */
/* 848 */  new yyActionEntry(  48,  36, -1   ), /*                     LP shift  36 */
/* 849 */  new yyActionEntry(  35,  33, -1   ), /*                     ID shift  33 */
/* 850 */  new yyActionEntry(  50, 112, -1   ), /*                  MINUS shift  112 */
/* 851 */  new yyActionEntry(  67,  35, 849  ), /*                 STRING shift  35 */
/* 852 */  new yyActionEntry(  52, 110, -1   ), /*                    NOT shift  110 */
/* 853 */  new yyActionEntry( 101,  58, -1   ), /*                     id shift  58 */
/* 854 */  new yyActionEntry(  54,  57, -1   ), /*                   NULL shift  57 */
/* 855 */  new yyActionEntry(130,0,-1), /* Unused */
/* 856 */  new yyActionEntry(  40,  61, -1   ), /*                INTEGER shift  61 */
/* 857 */  new yyActionEntry(130,0,-1), /* Unused */
/* 858 */  new yyActionEntry(  58, 114, -1   ), /*                   PLUS shift  114 */
/* 859 */  new yyActionEntry(  27,  62, -1   ), /*                  FLOAT shift  62 */
/* 860 */  new yyActionEntry(130,0,-1), /* Unused */
/* 861 */  new yyActionEntry(130,0,-1), /* Unused */
/* 862 */  new yyActionEntry(130,0,-1), /* Unused */
/* 863 */  new yyActionEntry(  95,  90, -1   ), /*                   expr shift  90 */
/* State 90 */
/* 864 */  new yyActionEntry(  12,  93, -1   ), /*                 CONCAT shift  93 */
/* State 91 */
/* 865 */  new yyActionEntry(  48,  36, -1   ), /*                     LP shift  36 */
/* 866 */  new yyActionEntry(  35,  33, -1   ), /*                     ID shift  33 */
/* 867 */  new yyActionEntry(  50, 112, -1   ), /*                  MINUS shift  112 */
/* 868 */  new yyActionEntry(  67,  35, 866  ), /*                 STRING shift  35 */
/* 869 */  new yyActionEntry(  52, 110, -1   ), /*                    NOT shift  110 */
/* 870 */  new yyActionEntry( 101,  58, -1   ), /*                     id shift  58 */
/* 871 */  new yyActionEntry(  54,  57, -1   ), /*                   NULL shift  57 */
/* 872 */  new yyActionEntry(130,0,-1), /* Unused */
/* 873 */  new yyActionEntry(  40,  61, -1   ), /*                INTEGER shift  61 */
/* 874 */  new yyActionEntry(130,0,-1), /* Unused */
/* 875 */  new yyActionEntry(  58, 114, -1   ), /*                   PLUS shift  114 */
/* 876 */  new yyActionEntry(  27,  62, -1   ), /*                  FLOAT shift  62 */
/* 877 */  new yyActionEntry(130,0,-1), /* Unused */
/* 878 */  new yyActionEntry(130,0,-1), /* Unused */
/* 879 */  new yyActionEntry(130,0,-1), /* Unused */
/* 880 */  new yyActionEntry(  95,  92, -1   ), /*                   expr shift  92 */
/* State 92 */
/* 881 */  new yyActionEntry(  12,  93, -1   ), /*                 CONCAT shift  93 */
/* State 93 */
/* 882 */  new yyActionEntry(  48,  36, -1   ), /*                     LP shift  36 */
/* 883 */  new yyActionEntry(  35,  33, -1   ), /*                     ID shift  33 */
/* 884 */  new yyActionEntry(  50, 112, -1   ), /*                  MINUS shift  112 */
/* 885 */  new yyActionEntry(  67,  35, 883  ), /*                 STRING shift  35 */
/* 886 */  new yyActionEntry(  52, 110, -1   ), /*                    NOT shift  110 */
/* 887 */  new yyActionEntry( 101,  58, -1   ), /*                     id shift  58 */
/* 888 */  new yyActionEntry(  54,  57, -1   ), /*                   NULL shift  57 */
/* 889 */  new yyActionEntry(130,0,-1), /* Unused */
/* 890 */  new yyActionEntry(  40,  61, -1   ), /*                INTEGER shift  61 */
/* 891 */  new yyActionEntry(130,0,-1), /* Unused */
/* 892 */  new yyActionEntry(  58, 114, -1   ), /*                   PLUS shift  114 */
/* 893 */  new yyActionEntry(  27,  62, -1   ), /*                  FLOAT shift  62 */
/* 894 */  new yyActionEntry(130,0,-1), /* Unused */
/* 895 */  new yyActionEntry(130,0,-1), /* Unused */
/* 896 */  new yyActionEntry(130,0,-1), /* Unused */
/* 897 */  new yyActionEntry(  95,  94, -1   ), /*                   expr shift  94 */
/* State 94 */
/* 898 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 95 */
/* 899 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 96 */
/* 900 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 97 */
/* 901 */  new yyActionEntry(  48,  36, -1   ), /*                     LP shift  36 */
/* 902 */  new yyActionEntry(  35,  33, -1   ), /*                     ID shift  33 */
/* 903 */  new yyActionEntry(  50, 112, -1   ), /*                  MINUS shift  112 */
/* 904 */  new yyActionEntry(  67,  35, 902  ), /*                 STRING shift  35 */
/* 905 */  new yyActionEntry(  52, 110, -1   ), /*                    NOT shift  110 */
/* 906 */  new yyActionEntry( 101,  58, -1   ), /*                     id shift  58 */
/* 907 */  new yyActionEntry(  54,  57, -1   ), /*                   NULL shift  57 */
/* 908 */  new yyActionEntry(130,0,-1), /* Unused */
/* 909 */  new yyActionEntry(  40,  61, -1   ), /*                INTEGER shift  61 */
/* 910 */  new yyActionEntry(130,0,-1), /* Unused */
/* 911 */  new yyActionEntry(  58, 114, -1   ), /*                   PLUS shift  114 */
/* 912 */  new yyActionEntry(  27,  62, -1   ), /*                  FLOAT shift  62 */
/* 913 */  new yyActionEntry(130,0,-1), /* Unused */
/* 914 */  new yyActionEntry(130,0,-1), /* Unused */
/* 915 */  new yyActionEntry(130,0,-1), /* Unused */
/* 916 */  new yyActionEntry(  95,  98, -1   ), /*                   expr shift  98 */
/* State 98 */
/* 917 */  new yyActionEntry(  64,  91, -1   ), /*                  SLASH shift  91 */
/* 918 */  new yyActionEntry(  33,  68, -1   ), /*                     GT shift  68 */
/* 919 */  new yyActionEntry(  66,  89, -1   ), /*                   STAR shift  89 */
/* 920 */  new yyActionEntry(   3,  99, -1   ), /*                    AND shift  99 */
/* 921 */  new yyActionEntry(  12,  93, -1   ), /*                 CONCAT shift  93 */
/* 922 */  new yyActionEntry(  37, 101, -1   ), /*                     IN shift  101 */
/* 923 */  new yyActionEntry(   6,  97, -1   ), /*                BETWEEN shift  97 */
/* 924 */  new yyActionEntry(  24,  76, -1   ), /*                     EQ shift  76 */
/* 925 */  new yyActionEntry(130,0,-1), /* Unused */
/* 926 */  new yyActionEntry(130,0,-1), /* Unused */
/* 927 */  new yyActionEntry(130,0,-1), /* Unused */
/* 928 */  new yyActionEntry(130,0,-1), /* Unused */
/* 929 */  new yyActionEntry(  44,  95, 921  ), /*                 ISNULL shift  95 */
/* 930 */  new yyActionEntry(130,0,-1), /* Unused */
/* 931 */  new yyActionEntry(  46,  70, -1   ), /*                     LE shift  70 */
/* 932 */  new yyActionEntry(  47,  78, -1   ), /*                   LIKE shift  78 */
/* 933 */  new yyActionEntry(130,0,-1), /* Unused */
/* 934 */  new yyActionEntry(  49,  66, -1   ), /*                     LT shift  66 */
/* 935 */  new yyActionEntry(  50,  87, -1   ), /*                  MINUS shift  87 */
/* 936 */  new yyActionEntry(  51,  74, -1   ), /*                     NE shift  74 */
/* 937 */  new yyActionEntry(  52,  80, -1   ), /*                    NOT shift  80 */
/* 938 */  new yyActionEntry(  53,  96, -1   ), /*                NOTNULL shift  96 */
/* 939 */  new yyActionEntry(130,0,-1), /* Unused */
/* 940 */  new yyActionEntry(130,0,-1), /* Unused */
/* 941 */  new yyActionEntry(  56,  64, 924  ), /*                     OR shift  64 */
/* 942 */  new yyActionEntry(130,0,-1), /* Unused */
/* 943 */  new yyActionEntry(  58,  85, -1   ), /*                   PLUS shift  85 */
/* 944 */  new yyActionEntry(130,0,-1), /* Unused */
/* 945 */  new yyActionEntry(130,0,-1), /* Unused */
/* 946 */  new yyActionEntry(130,0,-1), /* Unused */
/* 947 */  new yyActionEntry(  30,  72, -1   ), /*                     GE shift  72 */
/* 948 */  new yyActionEntry(  31,  83, -1   ), /*                   GLOB shift  83 */
/* State 99 */
/* 949 */  new yyActionEntry(  48,  36, -1   ), /*                     LP shift  36 */
/* 950 */  new yyActionEntry(  35,  33, -1   ), /*                     ID shift  33 */
/* 951 */  new yyActionEntry(  50, 112, -1   ), /*                  MINUS shift  112 */
/* 952 */  new yyActionEntry(  67,  35, 950  ), /*                 STRING shift  35 */
/* 953 */  new yyActionEntry(  52, 110, -1   ), /*                    NOT shift  110 */
/* 954 */  new yyActionEntry( 101,  58, -1   ), /*                     id shift  58 */
/* 955 */  new yyActionEntry(  54,  57, -1   ), /*                   NULL shift  57 */
/* 956 */  new yyActionEntry(130,0,-1), /* Unused */
/* 957 */  new yyActionEntry(  40,  61, -1   ), /*                INTEGER shift  61 */
/* 958 */  new yyActionEntry(130,0,-1), /* Unused */
/* 959 */  new yyActionEntry(  58, 114, -1   ), /*                   PLUS shift  114 */
/* 960 */  new yyActionEntry(  27,  62, -1   ), /*                  FLOAT shift  62 */
/* 961 */  new yyActionEntry(130,0,-1), /* Unused */
/* 962 */  new yyActionEntry(130,0,-1), /* Unused */
/* 963 */  new yyActionEntry(130,0,-1), /* Unused */
/* 964 */  new yyActionEntry(  95, 100, -1   ), /*                   expr shift  100 */
/* State 100 */
/* 965 */  new yyActionEntry(  64,  91, 966  ), /*                  SLASH shift  91 */
/* 966 */  new yyActionEntry(   0, 448, -1   ), /*                      $ reduce 143 */
/* 967 */  new yyActionEntry(  66,  89, -1   ), /*                   STAR shift  89 */
/* 968 */  new yyActionEntry(  67, 448, 973  ), /*                 STRING reduce 143 */
/* 969 */  new yyActionEntry(   4, 448, -1   ), /*                     AS reduce 143 */
/* 970 */  new yyActionEntry(   5, 448, -1   ), /*                    ASC reduce 143 */
/* 971 */  new yyActionEntry(   6, 448, -1   ), /*                BETWEEN reduce 143 */
/* 972 */  new yyActionEntry(  71, 448, -1   ), /*                  UNION reduce 143 */
/* 973 */  new yyActionEntry(   3, 448, 974  ), /*                    AND reduce 143 */
/* 974 */  new yyActionEntry(   3, 425, -1   ), /*                    AND reduce 120 */
/* 975 */  new yyActionEntry(  10, 448, -1   ), /*                  COMMA reduce 143 */
/* 976 */  new yyActionEntry(  56, 425, -1   ), /*                     OR reduce 120 */
/* 977 */  new yyActionEntry(  12,  93, -1   ), /*                 CONCAT shift  93 */
/* 978 */  new yyActionEntry(  77, 448, -1   ), /*                  WHERE reduce 143 */
/* 979 */  new yyActionEntry(130,0,-1), /* Unused */
/* 980 */  new yyActionEntry(130,0,-1), /* Unused */
/* 981 */  new yyActionEntry(130,0,-1), /* Unused */
/* 982 */  new yyActionEntry(130,0,-1), /* Unused */
/* 983 */  new yyActionEntry(130,0,-1), /* Unused */
/* 984 */  new yyActionEntry(  19, 448, -1   ), /*                   DESC reduce 143 */
/* 985 */  new yyActionEntry(130,0,-1), /* Unused */
/* 986 */  new yyActionEntry(130,0,-1), /* Unused */
/* 987 */  new yyActionEntry(130,0,-1), /* Unused */
/* 988 */  new yyActionEntry(130,0,-1), /* Unused */
/* 989 */  new yyActionEntry(  24, 448, -1   ), /*                     EQ reduce 143 */
/* 990 */  new yyActionEntry(  25, 448, -1   ), /*                 EXCEPT reduce 143 */
/* 991 */  new yyActionEntry(130,0,-1), /* Unused */
/* 992 */  new yyActionEntry(130,0,-1), /* Unused */
/* 993 */  new yyActionEntry(  28, 448, -1   ), /*                   FROM reduce 143 */
/* 994 */  new yyActionEntry(130,0,-1), /* Unused */
/* 995 */  new yyActionEntry(  30,  72, -1   ), /*                     GE shift  72 */
/* 996 */  new yyActionEntry(  31, 448, -1   ), /*                   GLOB reduce 143 */
/* 997 */  new yyActionEntry(  32, 448, -1   ), /*                  GROUP reduce 143 */
/* 998 */  new yyActionEntry(  33,  68, -1   ), /*                     GT shift  68 */
/* 999 */  new yyActionEntry(  34, 448, -1   ), /*                 HAVING reduce 143 */
/* 1000 */  new yyActionEntry(  35, 448, -1   ), /*                     ID reduce 143 */
/* 1001 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1002 */  new yyActionEntry(  37, 448, -1   ), /*                     IN reduce 143 */
/* 1003 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1004 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1005 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1006 */  new yyActionEntry(  41, 448, -1   ), /*              INTERSECT reduce 143 */
/* 1007 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1008 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1009 */  new yyActionEntry(  44, 448, -1   ), /*                 ISNULL reduce 143 */
/* 1010 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1011 */  new yyActionEntry(  46,  70, -1   ), /*                     LE shift  70 */
/* 1012 */  new yyActionEntry(  47, 448, -1   ), /*                   LIKE reduce 143 */
/* 1013 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1014 */  new yyActionEntry(  49,  66, -1   ), /*                     LT shift  66 */
/* 1015 */  new yyActionEntry(  50,  87, -1   ), /*                  MINUS shift  87 */
/* 1016 */  new yyActionEntry(  51, 448, -1   ), /*                     NE reduce 143 */
/* 1017 */  new yyActionEntry(  52, 448, -1   ), /*                    NOT reduce 143 */
/* 1018 */  new yyActionEntry(  53, 448, -1   ), /*                NOTNULL reduce 143 */
/* 1019 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1020 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1021 */  new yyActionEntry(  56, 448, 976  ), /*                     OR reduce 143 */
/* 1022 */  new yyActionEntry(  57, 448, -1   ), /*                  ORDER reduce 143 */
/* 1023 */  new yyActionEntry(  58,  85, -1   ), /*                   PLUS shift  85 */
/* 1024 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1025 */  new yyActionEntry(  60, 448, -1   ), /*                     RP reduce 143 */
/* 1026 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1027 */  new yyActionEntry(  62, 448, -1   ), /*                   SEMI reduce 143 */
/* 1028 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 101 */
/* 1029 */  new yyActionEntry(  48, 102, -1   ), /*                     LP shift  102 */
/* State 102 */
/* 1030 */  new yyActionEntry(  96, 120, 1037 ), /*               expritem shift  120 */
/* 1031 */  new yyActionEntry(  97, 116, -1   ), /*               exprlist shift  116 */
/* 1032 */  new yyActionEntry(  50, 112, -1   ), /*                  MINUS shift  112 */
/* 1033 */  new yyActionEntry( 115, 103, 1039 ), /*                 select shift  103 */
/* 1034 */  new yyActionEntry(  52, 110, -1   ), /*                    NOT shift  110 */
/* 1035 */  new yyActionEntry( 101,  58, -1   ), /*                     id shift  58 */
/* 1036 */  new yyActionEntry(  54,  57, -1   ), /*                   NULL shift  57 */
/* 1037 */  new yyActionEntry(  48,  36, -1   ), /*                     LP shift  36 */
/* 1038 */  new yyActionEntry(  40,  61, -1   ), /*                INTEGER shift  61 */
/* 1039 */  new yyActionEntry(  67,  35, 1042 ), /*                 STRING shift  35 */
/* 1040 */  new yyActionEntry(  58, 114, -1   ), /*                   PLUS shift  114 */
/* 1041 */  new yyActionEntry(  27,  62, -1   ), /*                  FLOAT shift  62 */
/* 1042 */  new yyActionEntry(  35,  33, -1   ), /*                     ID shift  33 */
/* 1043 */  new yyActionEntry(  61,  41, -1   ), /*                 SELECT shift  41 */
/* 1044 */  new yyActionEntry(  95, 109, -1   ), /*                   expr shift  109 */
/* 1045 */  new yyActionEntry( 111,  37, 1044 ), /*              oneselect shift  37 */
/* State 103 */
/* 1046 */  new yyActionEntry(  25, 107, -1   ), /*                 EXCEPT shift  107 */
/* 1047 */  new yyActionEntry(  41, 106, 1046 ), /*              INTERSECT shift  106 */
/* 1048 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1049 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1050 */  new yyActionEntry(  60, 108, -1   ), /*                     RP shift  108 */
/* 1051 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1052 */  new yyActionEntry( 110,  39, -1   ), /*                 joinop shift  39 */
/* 1053 */  new yyActionEntry(  71, 104, -1   ), /*                  UNION shift  104 */
/* State 104 */
/* 1054 */  new yyActionEntry(   2, 105, -1   ), /*                    ALL shift  105 */
/* 1055 */  new yyActionEntry(  61, 362, -1   ), /*                 SELECT reduce 57 */
/* State 105 */
/* 1056 */  new yyActionEntry(  61, 363, -1   ), /*                 SELECT reduce 58 */
/* State 106 */
/* 1057 */  new yyActionEntry(  61, 364, -1   ), /*                 SELECT reduce 59 */
/* State 107 */
/* 1058 */  new yyActionEntry(  61, 365, -1   ), /*                 SELECT reduce 60 */
/* State 108 */
/* 1059 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 109 */
/* 1060 */  new yyActionEntry(  64,  91, -1   ), /*                  SLASH shift  91 */
/* 1061 */  new yyActionEntry(  33,  68, -1   ), /*                     GT shift  68 */
/* 1062 */  new yyActionEntry(  66,  89, -1   ), /*                   STAR shift  89 */
/* 1063 */  new yyActionEntry(   3,  56, -1   ), /*                    AND shift  56 */
/* 1064 */  new yyActionEntry(  12,  93, -1   ), /*                 CONCAT shift  93 */
/* 1065 */  new yyActionEntry(  37, 101, -1   ), /*                     IN shift  101 */
/* 1066 */  new yyActionEntry(   6,  97, -1   ), /*                BETWEEN shift  97 */
/* 1067 */  new yyActionEntry(  24,  76, -1   ), /*                     EQ shift  76 */
/* 1068 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1069 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1070 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1071 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1072 */  new yyActionEntry(  44,  95, 1064 ), /*                 ISNULL shift  95 */
/* 1073 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1074 */  new yyActionEntry(  46,  70, -1   ), /*                     LE shift  70 */
/* 1075 */  new yyActionEntry(  47,  78, -1   ), /*                   LIKE shift  78 */
/* 1076 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1077 */  new yyActionEntry(  49,  66, -1   ), /*                     LT shift  66 */
/* 1078 */  new yyActionEntry(  50,  87, -1   ), /*                  MINUS shift  87 */
/* 1079 */  new yyActionEntry(  51,  74, -1   ), /*                     NE shift  74 */
/* 1080 */  new yyActionEntry(  52,  80, -1   ), /*                    NOT shift  80 */
/* 1081 */  new yyActionEntry(  53,  96, -1   ), /*                NOTNULL shift  96 */
/* 1082 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1083 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1084 */  new yyActionEntry(  56,  64, 1067 ), /*                     OR shift  64 */
/* 1085 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1086 */  new yyActionEntry(  58,  85, -1   ), /*                   PLUS shift  85 */
/* 1087 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1088 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1089 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1090 */  new yyActionEntry(  30,  72, -1   ), /*                     GE shift  72 */
/* 1091 */  new yyActionEntry(  31,  83, -1   ), /*                   GLOB shift  83 */
/* State 110 */
/* 1092 */  new yyActionEntry(  48,  36, -1   ), /*                     LP shift  36 */
/* 1093 */  new yyActionEntry(  35,  33, -1   ), /*                     ID shift  33 */
/* 1094 */  new yyActionEntry(  50, 112, -1   ), /*                  MINUS shift  112 */
/* 1095 */  new yyActionEntry(  67,  35, 1093 ), /*                 STRING shift  35 */
/* 1096 */  new yyActionEntry(  52, 110, -1   ), /*                    NOT shift  110 */
/* 1097 */  new yyActionEntry( 101,  58, -1   ), /*                     id shift  58 */
/* 1098 */  new yyActionEntry(  54,  57, -1   ), /*                   NULL shift  57 */
/* 1099 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1100 */  new yyActionEntry(  40,  61, -1   ), /*                INTEGER shift  61 */
/* 1101 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1102 */  new yyActionEntry(  58, 114, -1   ), /*                   PLUS shift  114 */
/* 1103 */  new yyActionEntry(  27,  62, -1   ), /*                  FLOAT shift  62 */
/* 1104 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1105 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1106 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1107 */  new yyActionEntry(  95, 111, -1   ), /*                   expr shift  111 */
/* State 111 */
/* 1108 */  new yyActionEntry(  64,  91, -1   ), /*                  SLASH shift  91 */
/* 1109 */  new yyActionEntry(  33,  68, -1   ), /*                     GT shift  68 */
/* 1110 */  new yyActionEntry(  66,  89, -1   ), /*                   STAR shift  89 */
/* 1111 */  new yyActionEntry(  12,  93, -1   ), /*                 CONCAT shift  93 */
/* 1112 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1113 */  new yyActionEntry(  37, 101, -1   ), /*                     IN shift  101 */
/* 1114 */  new yyActionEntry(   6,  97, -1   ), /*                BETWEEN shift  97 */
/* 1115 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1116 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1117 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1118 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1119 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1120 */  new yyActionEntry(  44,  95, 1111 ), /*                 ISNULL shift  95 */
/* 1121 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1122 */  new yyActionEntry(  46,  70, -1   ), /*                     LE shift  70 */
/* 1123 */  new yyActionEntry(  47,  78, -1   ), /*                   LIKE shift  78 */
/* 1124 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1125 */  new yyActionEntry(  49,  66, -1   ), /*                     LT shift  66 */
/* 1126 */  new yyActionEntry(  50,  87, -1   ), /*                  MINUS shift  87 */
/* 1127 */  new yyActionEntry(  51,  74, -1   ), /*                     NE shift  74 */
/* 1128 */  new yyActionEntry(  52,  80, -1   ), /*                    NOT shift  80 */
/* 1129 */  new yyActionEntry(  53,  96, -1   ), /*                NOTNULL shift  96 */
/* 1130 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1131 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1132 */  new yyActionEntry(  24,  76, -1   ), /*                     EQ shift  76 */
/* 1133 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1134 */  new yyActionEntry(  58,  85, -1   ), /*                   PLUS shift  85 */
/* 1135 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1136 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1137 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1138 */  new yyActionEntry(  30,  72, -1   ), /*                     GE shift  72 */
/* 1139 */  new yyActionEntry(  31,  83, -1   ), /*                   GLOB shift  83 */
/* State 112 */
/* 1140 */  new yyActionEntry(  48,  36, -1   ), /*                     LP shift  36 */
/* 1141 */  new yyActionEntry(  35,  33, -1   ), /*                     ID shift  33 */
/* 1142 */  new yyActionEntry(  50, 112, -1   ), /*                  MINUS shift  112 */
/* 1143 */  new yyActionEntry(  67,  35, 1141 ), /*                 STRING shift  35 */
/* 1144 */  new yyActionEntry(  52, 110, -1   ), /*                    NOT shift  110 */
/* 1145 */  new yyActionEntry( 101,  58, -1   ), /*                     id shift  58 */
/* 1146 */  new yyActionEntry(  54,  57, -1   ), /*                   NULL shift  57 */
/* 1147 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1148 */  new yyActionEntry(  40,  61, -1   ), /*                INTEGER shift  61 */
/* 1149 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1150 */  new yyActionEntry(  58, 114, -1   ), /*                   PLUS shift  114 */
/* 1151 */  new yyActionEntry(  27,  62, -1   ), /*                  FLOAT shift  62 */
/* 1152 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1153 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1154 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1155 */  new yyActionEntry(  95, 113, -1   ), /*                   expr shift  113 */
/* State 113 */
/* 1156 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 114 */
/* 1157 */  new yyActionEntry(  48,  36, -1   ), /*                     LP shift  36 */
/* 1158 */  new yyActionEntry(  35,  33, -1   ), /*                     ID shift  33 */
/* 1159 */  new yyActionEntry(  50, 112, -1   ), /*                  MINUS shift  112 */
/* 1160 */  new yyActionEntry(  67,  35, 1158 ), /*                 STRING shift  35 */
/* 1161 */  new yyActionEntry(  52, 110, -1   ), /*                    NOT shift  110 */
/* 1162 */  new yyActionEntry( 101,  58, -1   ), /*                     id shift  58 */
/* 1163 */  new yyActionEntry(  54,  57, -1   ), /*                   NULL shift  57 */
/* 1164 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1165 */  new yyActionEntry(  40,  61, -1   ), /*                INTEGER shift  61 */
/* 1166 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1167 */  new yyActionEntry(  58, 114, -1   ), /*                   PLUS shift  114 */
/* 1168 */  new yyActionEntry(  27,  62, -1   ), /*                  FLOAT shift  62 */
/* 1169 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1170 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1171 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1172 */  new yyActionEntry(  95, 115, -1   ), /*                   expr shift  115 */
/* State 115 */
/* 1173 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 116 */
/* 1174 */  new yyActionEntry(  60, 117, 1175 ), /*                     RP shift  117 */
/* 1175 */  new yyActionEntry(  10, 118, -1   ), /*                  COMMA shift  118 */
/* State 117 */
/* 1176 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 118 */
/* 1177 */  new yyActionEntry(  96, 119, 1178 ), /*               expritem shift  119 */
/* 1178 */  new yyActionEntry(  48,  36, -1   ), /*                     LP shift  36 */
/* 1179 */  new yyActionEntry(  50, 112, -1   ), /*                  MINUS shift  112 */
/* 1180 */  new yyActionEntry(  67,  35, 1184 ), /*                 STRING shift  35 */
/* 1181 */  new yyActionEntry(  52, 110, -1   ), /*                    NOT shift  110 */
/* 1182 */  new yyActionEntry( 101,  58, -1   ), /*                     id shift  58 */
/* 1183 */  new yyActionEntry(  54,  57, -1   ), /*                   NULL shift  57 */
/* 1184 */  new yyActionEntry(  35,  33, -1   ), /*                     ID shift  33 */
/* 1185 */  new yyActionEntry(  40,  61, -1   ), /*                INTEGER shift  61 */
/* 1186 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1187 */  new yyActionEntry(  58, 114, -1   ), /*                   PLUS shift  114 */
/* 1188 */  new yyActionEntry(  27,  62, -1   ), /*                  FLOAT shift  62 */
/* 1189 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1190 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1191 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1192 */  new yyActionEntry(  95, 109, -1   ), /*                   expr shift  109 */
/* State 119 */
/* 1193 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 120 */
/* 1194 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 121 */
/* 1195 */  new yyActionEntry(  48,  36, -1   ), /*                     LP shift  36 */
/* 1196 */  new yyActionEntry(  35,  33, -1   ), /*                     ID shift  33 */
/* 1197 */  new yyActionEntry(  50, 112, -1   ), /*                  MINUS shift  112 */
/* 1198 */  new yyActionEntry(  67,  35, 1196 ), /*                 STRING shift  35 */
/* 1199 */  new yyActionEntry(  52, 110, -1   ), /*                    NOT shift  110 */
/* 1200 */  new yyActionEntry( 101,  58, -1   ), /*                     id shift  58 */
/* 1201 */  new yyActionEntry(  54,  57, -1   ), /*                   NULL shift  57 */
/* 1202 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1203 */  new yyActionEntry(  40,  61, -1   ), /*                INTEGER shift  61 */
/* 1204 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1205 */  new yyActionEntry(  58, 114, -1   ), /*                   PLUS shift  114 */
/* 1206 */  new yyActionEntry(  27,  62, -1   ), /*                  FLOAT shift  62 */
/* 1207 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1208 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1209 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1210 */  new yyActionEntry(  95, 122, -1   ), /*                   expr shift  122 */
/* State 122 */
/* 1211 */  new yyActionEntry(  64,  91, -1   ), /*                  SLASH shift  91 */
/* 1212 */  new yyActionEntry(  33,  68, -1   ), /*                     GT shift  68 */
/* 1213 */  new yyActionEntry(  66,  89, -1   ), /*                   STAR shift  89 */
/* 1214 */  new yyActionEntry(  12,  93, -1   ), /*                 CONCAT shift  93 */
/* 1215 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1216 */  new yyActionEntry(  37, 101, -1   ), /*                     IN shift  101 */
/* 1217 */  new yyActionEntry(   6,  97, -1   ), /*                BETWEEN shift  97 */
/* 1218 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1219 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1220 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1221 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1222 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1223 */  new yyActionEntry(  44,  95, 1214 ), /*                 ISNULL shift  95 */
/* 1224 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1225 */  new yyActionEntry(  46,  70, -1   ), /*                     LE shift  70 */
/* 1226 */  new yyActionEntry(  47,  78, -1   ), /*                   LIKE shift  78 */
/* 1227 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1228 */  new yyActionEntry(  49,  66, -1   ), /*                     LT shift  66 */
/* 1229 */  new yyActionEntry(  50,  87, -1   ), /*                  MINUS shift  87 */
/* 1230 */  new yyActionEntry(  51,  74, -1   ), /*                     NE shift  74 */
/* 1231 */  new yyActionEntry(  52,  80, -1   ), /*                    NOT shift  80 */
/* 1232 */  new yyActionEntry(  53,  96, -1   ), /*                NOTNULL shift  96 */
/* 1233 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1234 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1235 */  new yyActionEntry(  24,  76, -1   ), /*                     EQ shift  76 */
/* 1236 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1237 */  new yyActionEntry(  58,  85, -1   ), /*                   PLUS shift  85 */
/* 1238 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1239 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1240 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1241 */  new yyActionEntry(  30,  72, -1   ), /*                     GE shift  72 */
/* 1242 */  new yyActionEntry(  31,  83, -1   ), /*                   GLOB shift  83 */
/* State 123 */
/* 1243 */  new yyActionEntry(  48,  36, -1   ), /*                     LP shift  36 */
/* 1244 */  new yyActionEntry(  35,  33, -1   ), /*                     ID shift  33 */
/* 1245 */  new yyActionEntry(  50, 112, -1   ), /*                  MINUS shift  112 */
/* 1246 */  new yyActionEntry(  67,  35, 1244 ), /*                 STRING shift  35 */
/* 1247 */  new yyActionEntry(  52, 110, -1   ), /*                    NOT shift  110 */
/* 1248 */  new yyActionEntry( 101,  58, -1   ), /*                     id shift  58 */
/* 1249 */  new yyActionEntry(  54,  57, -1   ), /*                   NULL shift  57 */
/* 1250 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1251 */  new yyActionEntry(  40,  61, -1   ), /*                INTEGER shift  61 */
/* 1252 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1253 */  new yyActionEntry(  58, 114, -1   ), /*                   PLUS shift  114 */
/* 1254 */  new yyActionEntry(  27,  62, -1   ), /*                  FLOAT shift  62 */
/* 1255 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1256 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1257 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1258 */  new yyActionEntry(  95, 124, -1   ), /*                   expr shift  124 */
/* State 124 */
/* 1259 */  new yyActionEntry(  64,  91, -1   ), /*                  SLASH shift  91 */
/* 1260 */  new yyActionEntry(  33,  68, -1   ), /*                     GT shift  68 */
/* 1261 */  new yyActionEntry(  66,  89, -1   ), /*                   STAR shift  89 */
/* 1262 */  new yyActionEntry(   3, 125, -1   ), /*                    AND shift  125 */
/* 1263 */  new yyActionEntry(  12,  93, -1   ), /*                 CONCAT shift  93 */
/* 1264 */  new yyActionEntry(  37, 101, -1   ), /*                     IN shift  101 */
/* 1265 */  new yyActionEntry(   6,  97, -1   ), /*                BETWEEN shift  97 */
/* 1266 */  new yyActionEntry(  24,  76, -1   ), /*                     EQ shift  76 */
/* 1267 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1268 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1269 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1270 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1271 */  new yyActionEntry(  44,  95, 1263 ), /*                 ISNULL shift  95 */
/* 1272 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1273 */  new yyActionEntry(  46,  70, -1   ), /*                     LE shift  70 */
/* 1274 */  new yyActionEntry(  47,  78, -1   ), /*                   LIKE shift  78 */
/* 1275 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1276 */  new yyActionEntry(  49,  66, -1   ), /*                     LT shift  66 */
/* 1277 */  new yyActionEntry(  50,  87, -1   ), /*                  MINUS shift  87 */
/* 1278 */  new yyActionEntry(  51,  74, -1   ), /*                     NE shift  74 */
/* 1279 */  new yyActionEntry(  52,  80, -1   ), /*                    NOT shift  80 */
/* 1280 */  new yyActionEntry(  53,  96, -1   ), /*                NOTNULL shift  96 */
/* 1281 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1282 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1283 */  new yyActionEntry(  56,  64, 1266 ), /*                     OR shift  64 */
/* 1284 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1285 */  new yyActionEntry(  58,  85, -1   ), /*                   PLUS shift  85 */
/* 1286 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1287 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1288 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1289 */  new yyActionEntry(  30,  72, -1   ), /*                     GE shift  72 */
/* 1290 */  new yyActionEntry(  31,  83, -1   ), /*                   GLOB shift  83 */
/* State 125 */
/* 1291 */  new yyActionEntry(  48,  36, -1   ), /*                     LP shift  36 */
/* 1292 */  new yyActionEntry(  35,  33, -1   ), /*                     ID shift  33 */
/* 1293 */  new yyActionEntry(  50, 112, -1   ), /*                  MINUS shift  112 */
/* 1294 */  new yyActionEntry(  67,  35, 1292 ), /*                 STRING shift  35 */
/* 1295 */  new yyActionEntry(  52, 110, -1   ), /*                    NOT shift  110 */
/* 1296 */  new yyActionEntry( 101,  58, -1   ), /*                     id shift  58 */
/* 1297 */  new yyActionEntry(  54,  57, -1   ), /*                   NULL shift  57 */
/* 1298 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1299 */  new yyActionEntry(  40,  61, -1   ), /*                INTEGER shift  61 */
/* 1300 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1301 */  new yyActionEntry(  58, 114, -1   ), /*                   PLUS shift  114 */
/* 1302 */  new yyActionEntry(  27,  62, -1   ), /*                  FLOAT shift  62 */
/* 1303 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1304 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1305 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1306 */  new yyActionEntry(  95, 126, -1   ), /*                   expr shift  126 */
/* State 126 */
/* 1307 */  new yyActionEntry(  64,  91, 1308 ), /*                  SLASH shift  91 */
/* 1308 */  new yyActionEntry(   0, 449, -1   ), /*                      $ reduce 144 */
/* 1309 */  new yyActionEntry(  66,  89, -1   ), /*                   STAR shift  89 */
/* 1310 */  new yyActionEntry(  67, 449, 1315 ), /*                 STRING reduce 144 */
/* 1311 */  new yyActionEntry(   4, 449, -1   ), /*                     AS reduce 144 */
/* 1312 */  new yyActionEntry(   5, 449, -1   ), /*                    ASC reduce 144 */
/* 1313 */  new yyActionEntry(   6,  97, -1   ), /*                BETWEEN shift  97 */
/* 1314 */  new yyActionEntry(  71, 449, -1   ), /*                  UNION reduce 144 */
/* 1315 */  new yyActionEntry(   3, 449, 1316 ), /*                    AND reduce 144 */
/* 1316 */  new yyActionEntry(   3, 425, -1   ), /*                    AND reduce 120 */
/* 1317 */  new yyActionEntry(  10, 449, -1   ), /*                  COMMA reduce 144 */
/* 1318 */  new yyActionEntry(  56, 425, -1   ), /*                     OR reduce 120 */
/* 1319 */  new yyActionEntry(  12,  93, -1   ), /*                 CONCAT shift  93 */
/* 1320 */  new yyActionEntry(  77, 449, -1   ), /*                  WHERE reduce 144 */
/* 1321 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1322 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1323 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1324 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1325 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1326 */  new yyActionEntry(  19, 449, -1   ), /*                   DESC reduce 144 */
/* 1327 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1328 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1329 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1330 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1331 */  new yyActionEntry(  24,  76, -1   ), /*                     EQ shift  76 */
/* 1332 */  new yyActionEntry(  25, 449, -1   ), /*                 EXCEPT reduce 144 */
/* 1333 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1334 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1335 */  new yyActionEntry(  28, 449, -1   ), /*                   FROM reduce 144 */
/* 1336 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1337 */  new yyActionEntry(  30,  72, -1   ), /*                     GE shift  72 */
/* 1338 */  new yyActionEntry(  31,  83, -1   ), /*                   GLOB shift  83 */
/* 1339 */  new yyActionEntry(  32, 449, -1   ), /*                  GROUP reduce 144 */
/* 1340 */  new yyActionEntry(  33,  68, -1   ), /*                     GT shift  68 */
/* 1341 */  new yyActionEntry(  34, 449, -1   ), /*                 HAVING reduce 144 */
/* 1342 */  new yyActionEntry(  35, 449, -1   ), /*                     ID reduce 144 */
/* 1343 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1344 */  new yyActionEntry(  37, 101, -1   ), /*                     IN shift  101 */
/* 1345 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1346 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1347 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1348 */  new yyActionEntry(  41, 449, -1   ), /*              INTERSECT reduce 144 */
/* 1349 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1350 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1351 */  new yyActionEntry(  44,  95, -1   ), /*                 ISNULL shift  95 */
/* 1352 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1353 */  new yyActionEntry(  46,  70, -1   ), /*                     LE shift  70 */
/* 1354 */  new yyActionEntry(  47,  78, -1   ), /*                   LIKE shift  78 */
/* 1355 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1356 */  new yyActionEntry(  49,  66, -1   ), /*                     LT shift  66 */
/* 1357 */  new yyActionEntry(  50,  87, -1   ), /*                  MINUS shift  87 */
/* 1358 */  new yyActionEntry(  51,  74, -1   ), /*                     NE shift  74 */
/* 1359 */  new yyActionEntry(  52,  80, -1   ), /*                    NOT shift  80 */
/* 1360 */  new yyActionEntry(  53,  96, -1   ), /*                NOTNULL shift  96 */
/* 1361 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1362 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1363 */  new yyActionEntry(  56, 449, 1318 ), /*                     OR reduce 144 */
/* 1364 */  new yyActionEntry(  57, 449, -1   ), /*                  ORDER reduce 144 */
/* 1365 */  new yyActionEntry(  58,  85, -1   ), /*                   PLUS shift  85 */
/* 1366 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1367 */  new yyActionEntry(  60, 449, -1   ), /*                     RP reduce 144 */
/* 1368 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1369 */  new yyActionEntry(  62, 449, -1   ), /*                   SEMI reduce 144 */
/* 1370 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 127 */
/* 1371 */  new yyActionEntry(  48, 128, -1   ), /*                     LP shift  128 */
/* State 128 */
/* 1372 */  new yyActionEntry(  96, 120, 1379 ), /*               expritem shift  120 */
/* 1373 */  new yyActionEntry(  97, 131, -1   ), /*               exprlist shift  131 */
/* 1374 */  new yyActionEntry(  50, 112, -1   ), /*                  MINUS shift  112 */
/* 1375 */  new yyActionEntry( 115, 129, 1381 ), /*                 select shift  129 */
/* 1376 */  new yyActionEntry(  52, 110, -1   ), /*                    NOT shift  110 */
/* 1377 */  new yyActionEntry( 101,  58, -1   ), /*                     id shift  58 */
/* 1378 */  new yyActionEntry(  54,  57, -1   ), /*                   NULL shift  57 */
/* 1379 */  new yyActionEntry(  48,  36, -1   ), /*                     LP shift  36 */
/* 1380 */  new yyActionEntry(  40,  61, -1   ), /*                INTEGER shift  61 */
/* 1381 */  new yyActionEntry(  67,  35, 1384 ), /*                 STRING shift  35 */
/* 1382 */  new yyActionEntry(  58, 114, -1   ), /*                   PLUS shift  114 */
/* 1383 */  new yyActionEntry(  27,  62, -1   ), /*                  FLOAT shift  62 */
/* 1384 */  new yyActionEntry(  35,  33, -1   ), /*                     ID shift  33 */
/* 1385 */  new yyActionEntry(  61,  41, -1   ), /*                 SELECT shift  41 */
/* 1386 */  new yyActionEntry(  95, 109, -1   ), /*                   expr shift  109 */
/* 1387 */  new yyActionEntry( 111,  37, 1386 ), /*              oneselect shift  37 */
/* State 129 */
/* 1388 */  new yyActionEntry(  25, 107, -1   ), /*                 EXCEPT shift  107 */
/* 1389 */  new yyActionEntry(  41, 106, 1388 ), /*              INTERSECT shift  106 */
/* 1390 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1391 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1392 */  new yyActionEntry(  60, 130, -1   ), /*                     RP shift  130 */
/* 1393 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1394 */  new yyActionEntry( 110,  39, -1   ), /*                 joinop shift  39 */
/* 1395 */  new yyActionEntry(  71, 104, -1   ), /*                  UNION shift  104 */
/* State 130 */
/* 1396 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 131 */
/* 1397 */  new yyActionEntry(  60, 132, 1398 ), /*                     RP shift  132 */
/* 1398 */  new yyActionEntry(  10, 118, -1   ), /*                  COMMA shift  118 */
/* State 132 */
/* 1399 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 133 */
/* 1400 */  new yyActionEntry(   5,  28, -1   ), /*                    ASC shift  28 */
/* 1401 */  new yyActionEntry( 121, 134, 1400 ), /*              sortorder shift  134 */
/* 1402 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1403 */  new yyActionEntry(  19,  29, -1   ), /*                   DESC shift  29 */
/* State 134 */
/* 1404 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 135 */
/* 1405 */  new yyActionEntry(  48,  36, -1   ), /*                     LP shift  36 */
/* 1406 */  new yyActionEntry(  35,  33, -1   ), /*                     ID shift  33 */
/* 1407 */  new yyActionEntry(  50, 112, -1   ), /*                  MINUS shift  112 */
/* 1408 */  new yyActionEntry(  67,  35, 1406 ), /*                 STRING shift  35 */
/* 1409 */  new yyActionEntry(  52, 110, -1   ), /*                    NOT shift  110 */
/* 1410 */  new yyActionEntry( 101,  58, -1   ), /*                     id shift  58 */
/* 1411 */  new yyActionEntry(  54,  57, -1   ), /*                   NULL shift  57 */
/* 1412 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1413 */  new yyActionEntry(  40,  61, -1   ), /*                INTEGER shift  61 */
/* 1414 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1415 */  new yyActionEntry(  58, 114, -1   ), /*                   PLUS shift  114 */
/* 1416 */  new yyActionEntry(  27,  62, -1   ), /*                  FLOAT shift  62 */
/* 1417 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1418 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1419 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1420 */  new yyActionEntry(  95, 136, -1   ), /*                   expr shift  136 */
/* State 136 */
/* 1421 */  new yyActionEntry(  64,  91, -1   ), /*                  SLASH shift  91 */
/* 1422 */  new yyActionEntry(  33,  68, -1   ), /*                     GT shift  68 */
/* 1423 */  new yyActionEntry(  66,  89, -1   ), /*                   STAR shift  89 */
/* 1424 */  new yyActionEntry(   3,  56, -1   ), /*                    AND shift  56 */
/* 1425 */  new yyActionEntry(  12,  93, -1   ), /*                 CONCAT shift  93 */
/* 1426 */  new yyActionEntry(  37, 101, -1   ), /*                     IN shift  101 */
/* 1427 */  new yyActionEntry(   6,  97, -1   ), /*                BETWEEN shift  97 */
/* 1428 */  new yyActionEntry(  24,  76, -1   ), /*                     EQ shift  76 */
/* 1429 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1430 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1431 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1432 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1433 */  new yyActionEntry(  44,  95, 1425 ), /*                 ISNULL shift  95 */
/* 1434 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1435 */  new yyActionEntry(  46,  70, -1   ), /*                     LE shift  70 */
/* 1436 */  new yyActionEntry(  47,  78, -1   ), /*                   LIKE shift  78 */
/* 1437 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1438 */  new yyActionEntry(  49,  66, -1   ), /*                     LT shift  66 */
/* 1439 */  new yyActionEntry(  50,  87, -1   ), /*                  MINUS shift  87 */
/* 1440 */  new yyActionEntry(  51,  74, -1   ), /*                     NE shift  74 */
/* 1441 */  new yyActionEntry(  52,  80, -1   ), /*                    NOT shift  80 */
/* 1442 */  new yyActionEntry(  53,  96, -1   ), /*                NOTNULL shift  96 */
/* 1443 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1444 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1445 */  new yyActionEntry(  56,  64, 1428 ), /*                     OR shift  64 */
/* 1446 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1447 */  new yyActionEntry(  58,  85, -1   ), /*                   PLUS shift  85 */
/* 1448 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1449 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1450 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1451 */  new yyActionEntry(  30,  72, -1   ), /*                     GE shift  72 */
/* 1452 */  new yyActionEntry(  31,  83, -1   ), /*                   GLOB shift  83 */
/* State 137 */
/* 1453 */  new yyActionEntry(   7, 138, -1   ), /*                     BY shift  138 */
/* State 138 */
/* 1454 */  new yyActionEntry(  96, 120, 1461 ), /*               expritem shift  120 */
/* 1455 */  new yyActionEntry(  97, 139, -1   ), /*               exprlist shift  139 */
/* 1456 */  new yyActionEntry(  50, 112, -1   ), /*                  MINUS shift  112 */
/* 1457 */  new yyActionEntry(  67,  35, 1463 ), /*                 STRING shift  35 */
/* 1458 */  new yyActionEntry(  52, 110, -1   ), /*                    NOT shift  110 */
/* 1459 */  new yyActionEntry( 101,  58, -1   ), /*                     id shift  58 */
/* 1460 */  new yyActionEntry(  54,  57, -1   ), /*                   NULL shift  57 */
/* 1461 */  new yyActionEntry(  48,  36, -1   ), /*                     LP shift  36 */
/* 1462 */  new yyActionEntry(  40,  61, -1   ), /*                INTEGER shift  61 */
/* 1463 */  new yyActionEntry(  35,  33, -1   ), /*                     ID shift  33 */
/* 1464 */  new yyActionEntry(  58, 114, -1   ), /*                   PLUS shift  114 */
/* 1465 */  new yyActionEntry(  27,  62, -1   ), /*                  FLOAT shift  62 */
/* 1466 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1467 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1468 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1469 */  new yyActionEntry(  95, 109, -1   ), /*                   expr shift  109 */
/* State 139 */
/* 1470 */  new yyActionEntry(  10, 118, -1   ), /*                  COMMA shift  118 */
/* State 140 */
/* 1471 */  new yyActionEntry(  48,  36, -1   ), /*                     LP shift  36 */
/* 1472 */  new yyActionEntry(  35,  33, -1   ), /*                     ID shift  33 */
/* 1473 */  new yyActionEntry(  50, 112, -1   ), /*                  MINUS shift  112 */
/* 1474 */  new yyActionEntry(  67,  35, 1472 ), /*                 STRING shift  35 */
/* 1475 */  new yyActionEntry(  52, 110, -1   ), /*                    NOT shift  110 */
/* 1476 */  new yyActionEntry( 101,  58, -1   ), /*                     id shift  58 */
/* 1477 */  new yyActionEntry(  54,  57, -1   ), /*                   NULL shift  57 */
/* 1478 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1479 */  new yyActionEntry(  40,  61, -1   ), /*                INTEGER shift  61 */
/* 1480 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1481 */  new yyActionEntry(  58, 114, -1   ), /*                   PLUS shift  114 */
/* 1482 */  new yyActionEntry(  27,  62, -1   ), /*                  FLOAT shift  62 */
/* 1483 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1484 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1485 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1486 */  new yyActionEntry(  95, 141, -1   ), /*                   expr shift  141 */
/* State 141 */
/* 1487 */  new yyActionEntry(  64,  91, -1   ), /*                  SLASH shift  91 */
/* 1488 */  new yyActionEntry(  33,  68, -1   ), /*                     GT shift  68 */
/* 1489 */  new yyActionEntry(  66,  89, -1   ), /*                   STAR shift  89 */
/* 1490 */  new yyActionEntry(   3,  56, -1   ), /*                    AND shift  56 */
/* 1491 */  new yyActionEntry(  12,  93, -1   ), /*                 CONCAT shift  93 */
/* 1492 */  new yyActionEntry(  37, 101, -1   ), /*                     IN shift  101 */
/* 1493 */  new yyActionEntry(   6,  97, -1   ), /*                BETWEEN shift  97 */
/* 1494 */  new yyActionEntry(  24,  76, -1   ), /*                     EQ shift  76 */
/* 1495 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1496 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1497 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1498 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1499 */  new yyActionEntry(  44,  95, 1491 ), /*                 ISNULL shift  95 */
/* 1500 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1501 */  new yyActionEntry(  46,  70, -1   ), /*                     LE shift  70 */
/* 1502 */  new yyActionEntry(  47,  78, -1   ), /*                   LIKE shift  78 */
/* 1503 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1504 */  new yyActionEntry(  49,  66, -1   ), /*                     LT shift  66 */
/* 1505 */  new yyActionEntry(  50,  87, -1   ), /*                  MINUS shift  87 */
/* 1506 */  new yyActionEntry(  51,  74, -1   ), /*                     NE shift  74 */
/* 1507 */  new yyActionEntry(  52,  80, -1   ), /*                    NOT shift  80 */
/* 1508 */  new yyActionEntry(  53,  96, -1   ), /*                NOTNULL shift  96 */
/* 1509 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1510 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1511 */  new yyActionEntry(  56,  64, 1494 ), /*                     OR shift  64 */
/* 1512 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1513 */  new yyActionEntry(  58,  85, -1   ), /*                   PLUS shift  85 */
/* 1514 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1515 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1516 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1517 */  new yyActionEntry(  30,  72, -1   ), /*                     GE shift  72 */
/* 1518 */  new yyActionEntry(  31,  83, -1   ), /*                   GLOB shift  83 */
/* State 142 */
/* 1519 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 143 */
/* 1520 */  new yyActionEntry( 122, 146, 1521 ), /*             stl_prefix shift  146 */
/* 1521 */  new yyActionEntry( 116, 144, -1   ), /*             seltablist shift  144 */
/* State 144 */
/* 1522 */  new yyActionEntry(  10, 145, -1   ), /*                  COMMA shift  145 */
/* State 145 */
/* 1523 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 146 */
/* 1524 */  new yyActionEntry(  35,  19, -1   ), /*                     ID shift  19 */
/* 1525 */  new yyActionEntry( 101, 147, -1   ), /*                     id shift  147 */
/* 1526 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1527 */  new yyActionEntry(  67,  20, 1524 ), /*                 STRING shift  20 */
/* State 147 */
/* 1528 */  new yyActionEntry(  32, 380, 1529 ), /*                  GROUP reduce 75 */
/* 1529 */  new yyActionEntry(   0, 380, -1   ), /*                      $ reduce 75 */
/* 1530 */  new yyActionEntry(  34, 380, -1   ), /*                 HAVING reduce 75 */
/* 1531 */  new yyActionEntry(  67, 375, 1533 ), /*                 STRING reduce 70 */
/* 1532 */  new yyActionEntry(   4, 148, -1   ), /*                     AS shift  148 */
/* 1533 */  new yyActionEntry(  35, 375, -1   ), /*                     ID reduce 70 */
/* 1534 */  new yyActionEntry(  41, 380, 1536 ), /*              INTERSECT reduce 75 */
/* 1535 */  new yyActionEntry(  71, 380, -1   ), /*                  UNION reduce 75 */
/* 1536 */  new yyActionEntry(  25, 380, -1   ), /*                 EXCEPT reduce 75 */
/* 1537 */  new yyActionEntry(  57, 380, 1534 ), /*                  ORDER reduce 75 */
/* 1538 */  new yyActionEntry(  10, 380, -1   ), /*                  COMMA reduce 75 */
/* 1539 */  new yyActionEntry(  62, 380, -1   ), /*                   SEMI reduce 75 */
/* 1540 */  new yyActionEntry(  60, 380, -1   ), /*                     RP reduce 75 */
/* 1541 */  new yyActionEntry(  77, 380, -1   ), /*                  WHERE reduce 75 */
/* 1542 */  new yyActionEntry(  78, 149, 1539 ), /*                     as shift  149 */
/* 1543 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 148 */
/* 1544 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 149 */
/* 1545 */  new yyActionEntry(  35,  19, -1   ), /*                     ID shift  19 */
/* 1546 */  new yyActionEntry( 101, 150, -1   ), /*                     id shift  150 */
/* 1547 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1548 */  new yyActionEntry(  67,  20, 1545 ), /*                 STRING shift  20 */
/* State 150 */
/* 1549 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 151 */
/* 1550 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 152 */
/* 1551 */  new yyActionEntry(  48,  36, -1   ), /*                     LP shift  36 */
/* 1552 */  new yyActionEntry(  35,  33, -1   ), /*                     ID shift  33 */
/* 1553 */  new yyActionEntry(  50, 112, -1   ), /*                  MINUS shift  112 */
/* 1554 */  new yyActionEntry(  67,  35, 1552 ), /*                 STRING shift  35 */
/* 1555 */  new yyActionEntry(  52, 110, -1   ), /*                    NOT shift  110 */
/* 1556 */  new yyActionEntry( 101,  58, -1   ), /*                     id shift  58 */
/* 1557 */  new yyActionEntry(  54,  57, -1   ), /*                   NULL shift  57 */
/* 1558 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1559 */  new yyActionEntry(  40,  61, -1   ), /*                INTEGER shift  61 */
/* 1560 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1561 */  new yyActionEntry(  58, 114, -1   ), /*                   PLUS shift  114 */
/* 1562 */  new yyActionEntry(  27,  62, -1   ), /*                  FLOAT shift  62 */
/* 1563 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1564 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1565 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1566 */  new yyActionEntry(  95, 153, -1   ), /*                   expr shift  153 */
/* State 153 */
/* 1567 */  new yyActionEntry(  64,  91, -1   ), /*                  SLASH shift  91 */
/* 1568 */  new yyActionEntry(  33,  68, -1   ), /*                     GT shift  68 */
/* 1569 */  new yyActionEntry(  66,  89, -1   ), /*                   STAR shift  89 */
/* 1570 */  new yyActionEntry(  67, 375, 1574 ), /*                 STRING reduce 70 */
/* 1571 */  new yyActionEntry(   4, 148, -1   ), /*                     AS shift  148 */
/* 1572 */  new yyActionEntry(  37, 101, -1   ), /*                     IN shift  101 */
/* 1573 */  new yyActionEntry(   6,  97, -1   ), /*                BETWEEN shift  97 */
/* 1574 */  new yyActionEntry(  35, 375, 1575 ), /*                     ID reduce 70 */
/* 1575 */  new yyActionEntry(   3,  56, -1   ), /*                    AND shift  56 */
/* 1576 */  new yyActionEntry(  12,  93, -1   ), /*                 CONCAT shift  93 */
/* 1577 */  new yyActionEntry(  10, 373, -1   ), /*                  COMMA reduce 68 */
/* 1578 */  new yyActionEntry(  46,  70, -1   ), /*                     LE shift  70 */
/* 1579 */  new yyActionEntry(  44,  95, 1576 ), /*                 ISNULL shift  95 */
/* 1580 */  new yyActionEntry(  24,  76, -1   ), /*                     EQ shift  76 */
/* 1581 */  new yyActionEntry(  78, 154, 1578 ), /*                     as shift  154 */
/* 1582 */  new yyActionEntry(  47,  78, -1   ), /*                   LIKE shift  78 */
/* 1583 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1584 */  new yyActionEntry(  49,  66, -1   ), /*                     LT shift  66 */
/* 1585 */  new yyActionEntry(  50,  87, -1   ), /*                  MINUS shift  87 */
/* 1586 */  new yyActionEntry(  51,  74, -1   ), /*                     NE shift  74 */
/* 1587 */  new yyActionEntry(  52,  80, -1   ), /*                    NOT shift  80 */
/* 1588 */  new yyActionEntry(  53,  96, -1   ), /*                NOTNULL shift  96 */
/* 1589 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1590 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1591 */  new yyActionEntry(  56,  64, 1580 ), /*                     OR shift  64 */
/* 1592 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1593 */  new yyActionEntry(  58,  85, -1   ), /*                   PLUS shift  85 */
/* 1594 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1595 */  new yyActionEntry(  28, 373, -1   ), /*                   FROM reduce 68 */
/* 1596 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1597 */  new yyActionEntry(  30,  72, -1   ), /*                     GE shift  72 */
/* 1598 */  new yyActionEntry(  31,  83, -1   ), /*                   GLOB shift  83 */
/* State 154 */
/* 1599 */  new yyActionEntry(  35,  19, -1   ), /*                     ID shift  19 */
/* 1600 */  new yyActionEntry( 101, 155, -1   ), /*                     id shift  155 */
/* 1601 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1602 */  new yyActionEntry(  67,  20, 1599 ), /*                 STRING shift  20 */
/* State 155 */
/* 1603 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 156 */
/* 1604 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 157 */
/* 1605 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 158 */
/* 1606 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 159 */
/* 1607 */  new yyActionEntry(  64,  91, -1   ), /*                  SLASH shift  91 */
/* 1608 */  new yyActionEntry(  33,  68, -1   ), /*                     GT shift  68 */
/* 1609 */  new yyActionEntry(  66,  89, -1   ), /*                   STAR shift  89 */
/* 1610 */  new yyActionEntry(   3,  56, -1   ), /*                    AND shift  56 */
/* 1611 */  new yyActionEntry(  12,  93, -1   ), /*                 CONCAT shift  93 */
/* 1612 */  new yyActionEntry(  37, 101, -1   ), /*                     IN shift  101 */
/* 1613 */  new yyActionEntry(   6,  97, -1   ), /*                BETWEEN shift  97 */
/* 1614 */  new yyActionEntry(  24,  76, -1   ), /*                     EQ shift  76 */
/* 1615 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1616 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1617 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1618 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1619 */  new yyActionEntry(  44,  95, 1611 ), /*                 ISNULL shift  95 */
/* 1620 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1621 */  new yyActionEntry(  46,  70, -1   ), /*                     LE shift  70 */
/* 1622 */  new yyActionEntry(  47,  78, -1   ), /*                   LIKE shift  78 */
/* 1623 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1624 */  new yyActionEntry(  49,  66, -1   ), /*                     LT shift  66 */
/* 1625 */  new yyActionEntry(  50,  87, -1   ), /*                  MINUS shift  87 */
/* 1626 */  new yyActionEntry(  51,  74, -1   ), /*                     NE shift  74 */
/* 1627 */  new yyActionEntry(  52,  80, -1   ), /*                    NOT shift  80 */
/* 1628 */  new yyActionEntry(  53,  96, -1   ), /*                NOTNULL shift  96 */
/* 1629 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1630 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1631 */  new yyActionEntry(  56,  64, 1614 ), /*                     OR shift  64 */
/* 1632 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1633 */  new yyActionEntry(  58,  85, -1   ), /*                   PLUS shift  85 */
/* 1634 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1635 */  new yyActionEntry(  60, 160, -1   ), /*                     RP shift  160 */
/* 1636 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1637 */  new yyActionEntry(  30,  72, -1   ), /*                     GE shift  72 */
/* 1638 */  new yyActionEntry(  31,  83, -1   ), /*                   GLOB shift  83 */
/* State 160 */
/* 1639 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 161 */
/* 1640 */  new yyActionEntry(  60, 162, 1641 ), /*                     RP shift  162 */
/* 1641 */  new yyActionEntry(  10, 118, -1   ), /*                  COMMA shift  118 */
/* State 162 */
/* 1642 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 163 */
/* 1643 */  new yyActionEntry(  60, 164, -1   ), /*                     RP shift  164 */
/* State 164 */
/* 1644 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 165 */
/* 1645 */  new yyActionEntry(  64,  91, -1   ), /*                  SLASH shift  91 */
/* 1646 */  new yyActionEntry(  33,  68, -1   ), /*                     GT shift  68 */
/* 1647 */  new yyActionEntry(  66,  89, -1   ), /*                   STAR shift  89 */
/* 1648 */  new yyActionEntry(   3,  56, -1   ), /*                    AND shift  56 */
/* 1649 */  new yyActionEntry(  12,  93, -1   ), /*                 CONCAT shift  93 */
/* 1650 */  new yyActionEntry(  37, 101, -1   ), /*                     IN shift  101 */
/* 1651 */  new yyActionEntry(   6,  97, -1   ), /*                BETWEEN shift  97 */
/* 1652 */  new yyActionEntry(  24,  76, -1   ), /*                     EQ shift  76 */
/* 1653 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1654 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1655 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1656 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1657 */  new yyActionEntry(  44,  95, 1649 ), /*                 ISNULL shift  95 */
/* 1658 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1659 */  new yyActionEntry(  46,  70, -1   ), /*                     LE shift  70 */
/* 1660 */  new yyActionEntry(  47,  78, -1   ), /*                   LIKE shift  78 */
/* 1661 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1662 */  new yyActionEntry(  49,  66, -1   ), /*                     LT shift  66 */
/* 1663 */  new yyActionEntry(  50,  87, -1   ), /*                  MINUS shift  87 */
/* 1664 */  new yyActionEntry(  51,  74, -1   ), /*                     NE shift  74 */
/* 1665 */  new yyActionEntry(  52,  80, -1   ), /*                    NOT shift  80 */
/* 1666 */  new yyActionEntry(  53,  96, -1   ), /*                NOTNULL shift  96 */
/* 1667 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1668 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1669 */  new yyActionEntry(  56,  64, 1652 ), /*                     OR shift  64 */
/* 1670 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1671 */  new yyActionEntry(  58,  85, -1   ), /*                   PLUS shift  85 */
/* 1672 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1673 */  new yyActionEntry(  60, 166, -1   ), /*                     RP shift  166 */
/* 1674 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1675 */  new yyActionEntry(  30,  72, -1   ), /*                     GE shift  72 */
/* 1676 */  new yyActionEntry(  31,  83, -1   ), /*                   GLOB shift  83 */
/* State 166 */
/* 1677 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 167 */
/* 1678 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 168 */
/* 1679 */  new yyActionEntry(  40, 171, -1   ), /*                INTEGER shift  171 */
/* 1680 */  new yyActionEntry(  50, 175, -1   ), /*                  MINUS shift  175 */
/* 1681 */  new yyActionEntry(  58, 172, 1680 ), /*                   PLUS shift  172 */
/* 1682 */  new yyActionEntry(  67, 169, 1683 ), /*                 STRING shift  169 */
/* 1683 */  new yyActionEntry(  35, 170, 1684 ), /*                     ID shift  170 */
/* 1684 */  new yyActionEntry(  27, 178, -1   ), /*                  FLOAT shift  178 */
/* 1685 */  new yyActionEntry(  54, 179, -1   ), /*                   NULL shift  179 */
/* 1686 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 169 */
/* 1687 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 170 */
/* 1688 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 171 */
/* 1689 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 172 */
/* 1690 */  new yyActionEntry(  40, 173, -1   ), /*                INTEGER shift  173 */
/* 1691 */  new yyActionEntry(  27, 174, -1   ), /*                  FLOAT shift  174 */
/* State 173 */
/* 1692 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 174 */
/* 1693 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 175 */
/* 1694 */  new yyActionEntry(  40, 176, -1   ), /*                INTEGER shift  176 */
/* 1695 */  new yyActionEntry(  27, 177, -1   ), /*                  FLOAT shift  177 */
/* State 176 */
/* 1696 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 177 */
/* 1697 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 178 */
/* 1698 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 179 */
/* 1699 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 180 */
/* 1700 */  new yyActionEntry(  48, 181, -1   ), /*                     LP shift  181 */
/* 1701 */  new yyActionEntry( 101, 192, -1   ), /*                     id shift  192 */
/* 1702 */  new yyActionEntry(  35,  19, -1   ), /*                     ID shift  19 */
/* 1703 */  new yyActionEntry(  67,  20, 1702 ), /*                 STRING shift  20 */
/* State 181 */
/* 1704 */  new yyActionEntry(  40, 187, -1   ), /*                INTEGER shift  187 */
/* 1705 */  new yyActionEntry(  58, 188, 1707 ), /*                   PLUS shift  188 */
/* 1706 */  new yyActionEntry( 118, 182, 1705 ), /*                 signed shift  182 */
/* 1707 */  new yyActionEntry(  50, 190, -1   ), /*                  MINUS shift  190 */
/* State 182 */
/* 1708 */  new yyActionEntry(  60, 183, 1709 ), /*                     RP shift  183 */
/* 1709 */  new yyActionEntry(  10, 184, -1   ), /*                  COMMA shift  184 */
/* State 183 */
/* 1710 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 184 */
/* 1711 */  new yyActionEntry(  40, 187, -1   ), /*                INTEGER shift  187 */
/* 1712 */  new yyActionEntry(  58, 188, 1714 ), /*                   PLUS shift  188 */
/* 1713 */  new yyActionEntry( 118, 185, 1712 ), /*                 signed shift  185 */
/* 1714 */  new yyActionEntry(  50, 190, -1   ), /*                  MINUS shift  190 */
/* State 185 */
/* 1715 */  new yyActionEntry(  60, 186, -1   ), /*                     RP shift  186 */
/* State 186 */
/* 1716 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 187 */
/* 1717 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 188 */
/* 1718 */  new yyActionEntry(  40, 189, -1   ), /*                INTEGER shift  189 */
/* State 189 */
/* 1719 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 190 */
/* 1720 */  new yyActionEntry(  40, 191, -1   ), /*                INTEGER shift  191 */
/* State 191 */
/* 1721 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 192 */
/* 1722 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 193 */
/* 1723 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 194 */
/* 1724 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 195 */
/* 1725 */  new yyActionEntry(  60, 348, 1726 ), /*                     RP reduce 43 */
/* 1726 */  new yyActionEntry(  10, 196, -1   ), /*                  COMMA shift  196 */
/* State 196 */
/* 1727 */  new yyActionEntry(  72, 210, 1728 ), /*                 UNIQUE shift  210 */
/* 1728 */  new yyActionEntry(   8, 217, -1   ), /*                  CHECK shift  217 */
/* 1729 */  new yyActionEntry(  59, 201, -1   ), /*                PRIMARY shift  201 */
/* 1730 */  new yyActionEntry( 123, 197, 1729 ), /*                  tcons shift  197 */
/* 1731 */  new yyActionEntry( 124, 219, -1   ), /*                 tcons2 shift  219 */
/* 1732 */  new yyActionEntry(  13, 198, -1   ), /*             CONSTRAINT shift  198 */
/* 1733 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1734 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 197 */
/* 1735 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 198 */
/* 1736 */  new yyActionEntry(  35,  19, -1   ), /*                     ID shift  19 */
/* 1737 */  new yyActionEntry( 101, 199, -1   ), /*                     id shift  199 */
/* 1738 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1739 */  new yyActionEntry(  67,  20, 1736 ), /*                 STRING shift  20 */
/* State 199 */
/* 1740 */  new yyActionEntry( 124, 200, 1741 ), /*                 tcons2 shift  200 */
/* 1741 */  new yyActionEntry(  72, 210, 1742 ), /*                 UNIQUE shift  210 */
/* 1742 */  new yyActionEntry(   8, 217, -1   ), /*                  CHECK shift  217 */
/* 1743 */  new yyActionEntry(  59, 201, -1   ), /*                PRIMARY shift  201 */
/* State 200 */
/* 1744 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 201 */
/* 1745 */  new yyActionEntry(  45, 202, -1   ), /*                    KEY shift  202 */
/* State 202 */
/* 1746 */  new yyActionEntry(  48, 203, -1   ), /*                     LP shift  203 */
/* State 203 */
/* 1747 */  new yyActionEntry( 104, 204, -1   ), /*                idxlist shift  204 */
/* 1748 */  new yyActionEntry(  35,  19, -1   ), /*                     ID shift  19 */
/* 1749 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1750 */  new yyActionEntry(  67,  20, 1748 ), /*                 STRING shift  20 */
/* 1751 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1752 */  new yyActionEntry( 101, 208, -1   ), /*                     id shift  208 */
/* 1753 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1754 */  new yyActionEntry( 103, 209, -1   ), /*                idxitem shift  209 */
/* State 204 */
/* 1755 */  new yyActionEntry(  60, 205, 1756 ), /*                     RP shift  205 */
/* 1756 */  new yyActionEntry(  10, 206, -1   ), /*                  COMMA shift  206 */
/* State 205 */
/* 1757 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 206 */
/* 1758 */  new yyActionEntry(  67,  20, 1760 ), /*                 STRING shift  20 */
/* 1759 */  new yyActionEntry( 101, 208, -1   ), /*                     id shift  208 */
/* 1760 */  new yyActionEntry(  35,  19, -1   ), /*                     ID shift  19 */
/* 1761 */  new yyActionEntry( 103, 207, 1758 ), /*                idxitem shift  207 */
/* State 207 */
/* 1762 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 208 */
/* 1763 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 209 */
/* 1764 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 210 */
/* 1765 */  new yyActionEntry(  48, 211, -1   ), /*                     LP shift  211 */
/* State 211 */
/* 1766 */  new yyActionEntry(  35,  19, -1   ), /*                     ID shift  19 */
/* 1767 */  new yyActionEntry( 101, 216, -1   ), /*                     id shift  216 */
/* 1768 */  new yyActionEntry( 102, 212, -1   ), /*                 idlist shift  212 */
/* 1769 */  new yyActionEntry(  67,  20, 1766 ), /*                 STRING shift  20 */
/* State 212 */
/* 1770 */  new yyActionEntry(  60, 213, 1771 ), /*                     RP shift  213 */
/* 1771 */  new yyActionEntry(  10, 214, -1   ), /*                  COMMA shift  214 */
/* State 213 */
/* 1772 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 214 */
/* 1773 */  new yyActionEntry(  35,  19, -1   ), /*                     ID shift  19 */
/* 1774 */  new yyActionEntry( 101, 215, -1   ), /*                     id shift  215 */
/* 1775 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1776 */  new yyActionEntry(  67,  20, 1773 ), /*                 STRING shift  20 */
/* State 215 */
/* 1777 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 216 */
/* 1778 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 217 */
/* 1779 */  new yyActionEntry(  48,  36, -1   ), /*                     LP shift  36 */
/* 1780 */  new yyActionEntry(  35,  33, -1   ), /*                     ID shift  33 */
/* 1781 */  new yyActionEntry(  50, 112, -1   ), /*                  MINUS shift  112 */
/* 1782 */  new yyActionEntry(  67,  35, 1780 ), /*                 STRING shift  35 */
/* 1783 */  new yyActionEntry(  52, 110, -1   ), /*                    NOT shift  110 */
/* 1784 */  new yyActionEntry( 101,  58, -1   ), /*                     id shift  58 */
/* 1785 */  new yyActionEntry(  54,  57, -1   ), /*                   NULL shift  57 */
/* 1786 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1787 */  new yyActionEntry(  40,  61, -1   ), /*                INTEGER shift  61 */
/* 1788 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1789 */  new yyActionEntry(  58, 114, -1   ), /*                   PLUS shift  114 */
/* 1790 */  new yyActionEntry(  27,  62, -1   ), /*                  FLOAT shift  62 */
/* 1791 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1792 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1793 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1794 */  new yyActionEntry(  95, 218, -1   ), /*                   expr shift  218 */
/* State 218 */
/* 1795 */  new yyActionEntry(  64,  91, -1   ), /*                  SLASH shift  91 */
/* 1796 */  new yyActionEntry(  33,  68, -1   ), /*                     GT shift  68 */
/* 1797 */  new yyActionEntry(  66,  89, -1   ), /*                   STAR shift  89 */
/* 1798 */  new yyActionEntry(   3,  56, -1   ), /*                    AND shift  56 */
/* 1799 */  new yyActionEntry(  12,  93, -1   ), /*                 CONCAT shift  93 */
/* 1800 */  new yyActionEntry(  37, 101, -1   ), /*                     IN shift  101 */
/* 1801 */  new yyActionEntry(   6,  97, -1   ), /*                BETWEEN shift  97 */
/* 1802 */  new yyActionEntry(  24,  76, -1   ), /*                     EQ shift  76 */
/* 1803 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1804 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1805 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1806 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1807 */  new yyActionEntry(  44,  95, 1799 ), /*                 ISNULL shift  95 */
/* 1808 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1809 */  new yyActionEntry(  46,  70, -1   ), /*                     LE shift  70 */
/* 1810 */  new yyActionEntry(  47,  78, -1   ), /*                   LIKE shift  78 */
/* 1811 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1812 */  new yyActionEntry(  49,  66, -1   ), /*                     LT shift  66 */
/* 1813 */  new yyActionEntry(  50,  87, -1   ), /*                  MINUS shift  87 */
/* 1814 */  new yyActionEntry(  51,  74, -1   ), /*                     NE shift  74 */
/* 1815 */  new yyActionEntry(  52,  80, -1   ), /*                    NOT shift  80 */
/* 1816 */  new yyActionEntry(  53,  96, -1   ), /*                NOTNULL shift  96 */
/* 1817 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1818 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1819 */  new yyActionEntry(  56,  64, 1802 ), /*                     OR shift  64 */
/* 1820 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1821 */  new yyActionEntry(  58,  85, -1   ), /*                   PLUS shift  85 */
/* 1822 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1823 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1824 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1825 */  new yyActionEntry(  30,  72, -1   ), /*                     GE shift  72 */
/* 1826 */  new yyActionEntry(  31,  83, -1   ), /*                   GLOB shift  83 */
/* State 219 */
/* 1827 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 220 */
/* 1828 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 221 */
/* 1829 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 222 */
/* 1830 */  new yyActionEntry(  72, 233, 1831 ), /*                 UNIQUE shift  233 */
/* 1831 */  new yyActionEntry(  68, 223, -1   ), /*                  TABLE shift  223 */
/* 1832 */  new yyActionEntry(  38, 460, -1   ), /*                  INDEX reduce 155 */
/* 1833 */  new yyActionEntry( 127, 225, -1   ), /*             uniqueflag shift  225 */
/* State 223 */
/* 1834 */  new yyActionEntry(  35,  19, -1   ), /*                     ID shift  19 */
/* 1835 */  new yyActionEntry( 101, 224, -1   ), /*                     id shift  224 */
/* 1836 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1837 */  new yyActionEntry(  67,  20, 1834 ), /*                 STRING shift  20 */
/* State 224 */
/* 1838 */  new yyActionEntry(  48, 314, -1   ), /*                     LP reduce 9 */
/* State 225 */
/* 1839 */  new yyActionEntry(  38, 226, -1   ), /*                  INDEX shift  226 */
/* State 226 */
/* 1840 */  new yyActionEntry(  35,  19, -1   ), /*                     ID shift  19 */
/* 1841 */  new yyActionEntry( 101, 227, -1   ), /*                     id shift  227 */
/* 1842 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1843 */  new yyActionEntry(  67,  20, 1840 ), /*                 STRING shift  20 */
/* State 227 */
/* 1844 */  new yyActionEntry(  55, 228, -1   ), /*                     ON shift  228 */
/* State 228 */
/* 1845 */  new yyActionEntry(  35,  19, -1   ), /*                     ID shift  19 */
/* 1846 */  new yyActionEntry( 101, 229, -1   ), /*                     id shift  229 */
/* 1847 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1848 */  new yyActionEntry(  67,  20, 1845 ), /*                 STRING shift  20 */
/* State 229 */
/* 1849 */  new yyActionEntry(  48, 230, -1   ), /*                     LP shift  230 */
/* State 230 */
/* 1850 */  new yyActionEntry( 104, 231, -1   ), /*                idxlist shift  231 */
/* 1851 */  new yyActionEntry(  35,  19, -1   ), /*                     ID shift  19 */
/* 1852 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1853 */  new yyActionEntry(  67,  20, 1851 ), /*                 STRING shift  20 */
/* 1854 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1855 */  new yyActionEntry( 101, 208, -1   ), /*                     id shift  208 */
/* 1856 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1857 */  new yyActionEntry( 103, 209, -1   ), /*                idxitem shift  209 */
/* State 231 */
/* 1858 */  new yyActionEntry(  60, 232, 1859 ), /*                     RP shift  232 */
/* 1859 */  new yyActionEntry(  10, 206, -1   ), /*                  COMMA shift  206 */
/* State 232 */
/* 1860 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 233 */
/* 1861 */  new yyActionEntry(  38, 459, -1   ), /*                  INDEX reduce 154 */
/* State 234 */
/* 1862 */  new yyActionEntry(  68, 235, 1863 ), /*                  TABLE shift  235 */
/* 1863 */  new yyActionEntry(  38, 237, -1   ), /*                  INDEX shift  237 */
/* State 235 */
/* 1864 */  new yyActionEntry(  35,  19, -1   ), /*                     ID shift  19 */
/* 1865 */  new yyActionEntry( 101, 236, -1   ), /*                     id shift  236 */
/* 1866 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1867 */  new yyActionEntry(  67,  20, 1864 ), /*                 STRING shift  20 */
/* State 236 */
/* 1868 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 237 */
/* 1869 */  new yyActionEntry(  35,  19, -1   ), /*                     ID shift  19 */
/* 1870 */  new yyActionEntry( 101, 238, -1   ), /*                     id shift  238 */
/* 1871 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1872 */  new yyActionEntry(  67,  20, 1869 ), /*                 STRING shift  20 */
/* State 238 */
/* 1873 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 239 */
/* 1874 */  new yyActionEntry(  25, 107, -1   ), /*                 EXCEPT shift  107 */
/* 1875 */  new yyActionEntry(  41, 106, 1874 ), /*              INTERSECT shift  106 */
/* 1876 */  new yyActionEntry( 110,  39, -1   ), /*                 joinop shift  39 */
/* 1877 */  new yyActionEntry(  71, 104, -1   ), /*                  UNION shift  104 */
/* State 240 */
/* 1878 */  new yyActionEntry(  28, 241, -1   ), /*                   FROM shift  241 */
/* State 241 */
/* 1879 */  new yyActionEntry(  35,  19, -1   ), /*                     ID shift  19 */
/* 1880 */  new yyActionEntry( 101, 242, -1   ), /*                     id shift  242 */
/* 1881 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1882 */  new yyActionEntry(  67,  20, 1879 ), /*                 STRING shift  20 */
/* State 242 */
/* 1883 */  new yyActionEntry( 128, 243, -1   ), /*              where_opt shift  243 */
/* 1884 */  new yyActionEntry(  77, 140, -1   ), /*                  WHERE shift  140 */
/* State 243 */
/* 1885 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 244 */
/* 1886 */  new yyActionEntry(  35,  19, -1   ), /*                     ID shift  19 */
/* 1887 */  new yyActionEntry( 101, 245, -1   ), /*                     id shift  245 */
/* 1888 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1889 */  new yyActionEntry(  67,  20, 1886 ), /*                 STRING shift  20 */
/* State 245 */
/* 1890 */  new yyActionEntry(  63, 246, -1   ), /*                    SET shift  246 */
/* State 246 */
/* 1891 */  new yyActionEntry( 101, 253, -1   ), /*                     id shift  253 */
/* 1892 */  new yyActionEntry( 117, 247, 1891 ), /*                setlist shift  247 */
/* 1893 */  new yyActionEntry(  35,  19, -1   ), /*                     ID shift  19 */
/* 1894 */  new yyActionEntry(  67,  20, 1893 ), /*                 STRING shift  20 */
/* State 247 */
/* 1895 */  new yyActionEntry( 128, 248, -1   ), /*              where_opt shift  248 */
/* 1896 */  new yyActionEntry(  77, 140, -1   ), /*                  WHERE shift  140 */
/* 1897 */  new yyActionEntry(  10, 249, -1   ), /*                  COMMA shift  249 */
/* 1898 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 248 */
/* 1899 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 249 */
/* 1900 */  new yyActionEntry(  35,  19, -1   ), /*                     ID shift  19 */
/* 1901 */  new yyActionEntry( 101, 250, -1   ), /*                     id shift  250 */
/* 1902 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1903 */  new yyActionEntry(  67,  20, 1900 ), /*                 STRING shift  20 */
/* State 250 */
/* 1904 */  new yyActionEntry(  24, 251, -1   ), /*                     EQ shift  251 */
/* State 251 */
/* 1905 */  new yyActionEntry(  48,  36, -1   ), /*                     LP shift  36 */
/* 1906 */  new yyActionEntry(  35,  33, -1   ), /*                     ID shift  33 */
/* 1907 */  new yyActionEntry(  50, 112, -1   ), /*                  MINUS shift  112 */
/* 1908 */  new yyActionEntry(  67,  35, 1906 ), /*                 STRING shift  35 */
/* 1909 */  new yyActionEntry(  52, 110, -1   ), /*                    NOT shift  110 */
/* 1910 */  new yyActionEntry( 101,  58, -1   ), /*                     id shift  58 */
/* 1911 */  new yyActionEntry(  54,  57, -1   ), /*                   NULL shift  57 */
/* 1912 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1913 */  new yyActionEntry(  40,  61, -1   ), /*                INTEGER shift  61 */
/* 1914 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1915 */  new yyActionEntry(  58, 114, -1   ), /*                   PLUS shift  114 */
/* 1916 */  new yyActionEntry(  27,  62, -1   ), /*                  FLOAT shift  62 */
/* 1917 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1918 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1919 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1920 */  new yyActionEntry(  95, 252, -1   ), /*                   expr shift  252 */
/* State 252 */
/* 1921 */  new yyActionEntry(  64,  91, -1   ), /*                  SLASH shift  91 */
/* 1922 */  new yyActionEntry(  33,  68, -1   ), /*                     GT shift  68 */
/* 1923 */  new yyActionEntry(  66,  89, -1   ), /*                   STAR shift  89 */
/* 1924 */  new yyActionEntry(   3,  56, -1   ), /*                    AND shift  56 */
/* 1925 */  new yyActionEntry(  12,  93, -1   ), /*                 CONCAT shift  93 */
/* 1926 */  new yyActionEntry(  37, 101, -1   ), /*                     IN shift  101 */
/* 1927 */  new yyActionEntry(   6,  97, -1   ), /*                BETWEEN shift  97 */
/* 1928 */  new yyActionEntry(  24,  76, -1   ), /*                     EQ shift  76 */
/* 1929 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1930 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1931 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1932 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1933 */  new yyActionEntry(  44,  95, 1925 ), /*                 ISNULL shift  95 */
/* 1934 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1935 */  new yyActionEntry(  46,  70, -1   ), /*                     LE shift  70 */
/* 1936 */  new yyActionEntry(  47,  78, -1   ), /*                   LIKE shift  78 */
/* 1937 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1938 */  new yyActionEntry(  49,  66, -1   ), /*                     LT shift  66 */
/* 1939 */  new yyActionEntry(  50,  87, -1   ), /*                  MINUS shift  87 */
/* 1940 */  new yyActionEntry(  51,  74, -1   ), /*                     NE shift  74 */
/* 1941 */  new yyActionEntry(  52,  80, -1   ), /*                    NOT shift  80 */
/* 1942 */  new yyActionEntry(  53,  96, -1   ), /*                NOTNULL shift  96 */
/* 1943 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1944 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1945 */  new yyActionEntry(  56,  64, 1928 ), /*                     OR shift  64 */
/* 1946 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1947 */  new yyActionEntry(  58,  85, -1   ), /*                   PLUS shift  85 */
/* 1948 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1949 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1950 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1951 */  new yyActionEntry(  30,  72, -1   ), /*                     GE shift  72 */
/* 1952 */  new yyActionEntry(  31,  83, -1   ), /*                   GLOB shift  83 */
/* State 253 */
/* 1953 */  new yyActionEntry(  24, 254, -1   ), /*                     EQ shift  254 */
/* State 254 */
/* 1954 */  new yyActionEntry(  48,  36, -1   ), /*                     LP shift  36 */
/* 1955 */  new yyActionEntry(  35,  33, -1   ), /*                     ID shift  33 */
/* 1956 */  new yyActionEntry(  50, 112, -1   ), /*                  MINUS shift  112 */
/* 1957 */  new yyActionEntry(  67,  35, 1955 ), /*                 STRING shift  35 */
/* 1958 */  new yyActionEntry(  52, 110, -1   ), /*                    NOT shift  110 */
/* 1959 */  new yyActionEntry( 101,  58, -1   ), /*                     id shift  58 */
/* 1960 */  new yyActionEntry(  54,  57, -1   ), /*                   NULL shift  57 */
/* 1961 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1962 */  new yyActionEntry(  40,  61, -1   ), /*                INTEGER shift  61 */
/* 1963 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1964 */  new yyActionEntry(  58, 114, -1   ), /*                   PLUS shift  114 */
/* 1965 */  new yyActionEntry(  27,  62, -1   ), /*                  FLOAT shift  62 */
/* 1966 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1967 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1968 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1969 */  new yyActionEntry(  95, 255, -1   ), /*                   expr shift  255 */
/* State 255 */
/* 1970 */  new yyActionEntry(  64,  91, -1   ), /*                  SLASH shift  91 */
/* 1971 */  new yyActionEntry(  33,  68, -1   ), /*                     GT shift  68 */
/* 1972 */  new yyActionEntry(  66,  89, -1   ), /*                   STAR shift  89 */
/* 1973 */  new yyActionEntry(   3,  56, -1   ), /*                    AND shift  56 */
/* 1974 */  new yyActionEntry(  12,  93, -1   ), /*                 CONCAT shift  93 */
/* 1975 */  new yyActionEntry(  37, 101, -1   ), /*                     IN shift  101 */
/* 1976 */  new yyActionEntry(   6,  97, -1   ), /*                BETWEEN shift  97 */
/* 1977 */  new yyActionEntry(  24,  76, -1   ), /*                     EQ shift  76 */
/* 1978 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1979 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1980 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1981 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1982 */  new yyActionEntry(  44,  95, 1974 ), /*                 ISNULL shift  95 */
/* 1983 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1984 */  new yyActionEntry(  46,  70, -1   ), /*                     LE shift  70 */
/* 1985 */  new yyActionEntry(  47,  78, -1   ), /*                   LIKE shift  78 */
/* 1986 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1987 */  new yyActionEntry(  49,  66, -1   ), /*                     LT shift  66 */
/* 1988 */  new yyActionEntry(  50,  87, -1   ), /*                  MINUS shift  87 */
/* 1989 */  new yyActionEntry(  51,  74, -1   ), /*                     NE shift  74 */
/* 1990 */  new yyActionEntry(  52,  80, -1   ), /*                    NOT shift  80 */
/* 1991 */  new yyActionEntry(  53,  96, -1   ), /*                NOTNULL shift  96 */
/* 1992 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1993 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1994 */  new yyActionEntry(  56,  64, 1977 ), /*                     OR shift  64 */
/* 1995 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1996 */  new yyActionEntry(  58,  85, -1   ), /*                   PLUS shift  85 */
/* 1997 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1998 */  new yyActionEntry(130,0,-1), /* Unused */
/* 1999 */  new yyActionEntry(130,0,-1), /* Unused */
/* 2000 */  new yyActionEntry(  30,  72, -1   ), /*                     GE shift  72 */
/* 2001 */  new yyActionEntry(  31,  83, -1   ), /*                   GLOB shift  83 */
/* State 256 */
/* 2002 */  new yyActionEntry(  42, 257, -1   ), /*                   INTO shift  257 */
/* State 257 */
/* 2003 */  new yyActionEntry(  35,  19, -1   ), /*                     ID shift  19 */
/* 2004 */  new yyActionEntry( 101, 258, -1   ), /*                     id shift  258 */
/* 2005 */  new yyActionEntry(130,0,-1), /* Unused */
/* 2006 */  new yyActionEntry(  67,  20, 2003 ), /*                 STRING shift  20 */
/* State 258 */
/* 2007 */  new yyActionEntry(  48, 278, -1   ), /*                     LP shift  278 */
/* 2008 */  new yyActionEntry( 107, 259, -1   ), /*         inscollist_opt shift  259 */
/* State 259 */
/* 2009 */  new yyActionEntry(  76, 261, -1   ), /*                 VALUES shift  261 */
/* 2010 */  new yyActionEntry(  61,  41, -1   ), /*                 SELECT shift  41 */
/* 2011 */  new yyActionEntry( 111,  37, -1   ), /*              oneselect shift  37 */
/* 2012 */  new yyActionEntry( 115, 260, 2011 ), /*                 select shift  260 */
/* State 260 */
/* 2013 */  new yyActionEntry(  25, 107, -1   ), /*                 EXCEPT shift  107 */
/* 2014 */  new yyActionEntry(  41, 106, 2013 ), /*              INTERSECT shift  106 */
/* 2015 */  new yyActionEntry( 110,  39, -1   ), /*                 joinop shift  39 */
/* 2016 */  new yyActionEntry(  71, 104, -1   ), /*                  UNION shift  104 */
/* State 261 */
/* 2017 */  new yyActionEntry(  48, 262, -1   ), /*                     LP shift  262 */
/* State 262 */
/* 2018 */  new yyActionEntry(  40, 267, -1   ), /*                INTEGER shift  267 */
/* 2019 */  new yyActionEntry(  50, 271, -1   ), /*                  MINUS shift  271 */
/* 2020 */  new yyActionEntry(  58, 268, 2019 ), /*                   PLUS shift  268 */
/* 2021 */  new yyActionEntry(  67, 275, 2025 ), /*                 STRING shift  275 */
/* 2022 */  new yyActionEntry( 108, 277, -1   ), /*                   item shift  277 */
/* 2023 */  new yyActionEntry( 109, 263, -1   ), /*               itemlist shift  263 */
/* 2024 */  new yyActionEntry(  54, 276, -1   ), /*                   NULL shift  276 */
/* 2025 */  new yyActionEntry(  27, 274, -1   ), /*                  FLOAT shift  274 */
/* State 263 */
/* 2026 */  new yyActionEntry(  60, 264, 2027 ), /*                     RP shift  264 */
/* 2027 */  new yyActionEntry(  10, 265, -1   ), /*                  COMMA shift  265 */
/* State 264 */
/* 2028 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 265 */
/* 2029 */  new yyActionEntry(  40, 267, -1   ), /*                INTEGER shift  267 */
/* 2030 */  new yyActionEntry(  50, 271, -1   ), /*                  MINUS shift  271 */
/* 2031 */  new yyActionEntry(  58, 268, 2030 ), /*                   PLUS shift  268 */
/* 2032 */  new yyActionEntry(  67, 275, 2034 ), /*                 STRING shift  275 */
/* 2033 */  new yyActionEntry( 108, 266, -1   ), /*                   item shift  266 */
/* 2034 */  new yyActionEntry(  27, 274, -1   ), /*                  FLOAT shift  274 */
/* 2035 */  new yyActionEntry(  54, 276, -1   ), /*                   NULL shift  276 */
/* 2036 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 266 */
/* 2037 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 267 */
/* 2038 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 268 */
/* 2039 */  new yyActionEntry(  40, 269, -1   ), /*                INTEGER shift  269 */
/* 2040 */  new yyActionEntry(  27, 270, -1   ), /*                  FLOAT shift  270 */
/* State 269 */
/* 2041 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 270 */
/* 2042 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 271 */
/* 2043 */  new yyActionEntry(  40, 272, -1   ), /*                INTEGER shift  272 */
/* 2044 */  new yyActionEntry(  27, 273, -1   ), /*                  FLOAT shift  273 */
/* State 272 */
/* 2045 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 273 */
/* 2046 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 274 */
/* 2047 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 275 */
/* 2048 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 276 */
/* 2049 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 277 */
/* 2050 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 278 */
/* 2051 */  new yyActionEntry(  35,  19, -1   ), /*                     ID shift  19 */
/* 2052 */  new yyActionEntry( 101, 283, -1   ), /*                     id shift  283 */
/* 2053 */  new yyActionEntry( 106, 279, -1   ), /*             inscollist shift  279 */
/* 2054 */  new yyActionEntry(  67,  20, 2051 ), /*                 STRING shift  20 */
/* State 279 */
/* 2055 */  new yyActionEntry(  60, 280, 2056 ), /*                     RP shift  280 */
/* 2056 */  new yyActionEntry(  10, 281, -1   ), /*                  COMMA shift  281 */
/* State 280 */
/* 2057 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 281 */
/* 2058 */  new yyActionEntry(  35,  19, -1   ), /*                     ID shift  19 */
/* 2059 */  new yyActionEntry( 101, 282, -1   ), /*                     id shift  282 */
/* 2060 */  new yyActionEntry(130,0,-1), /* Unused */
/* 2061 */  new yyActionEntry(  67,  20, 2058 ), /*                 STRING shift  20 */
/* State 282 */
/* 2062 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 283 */
/* 2063 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 284 */
/* 2064 */  new yyActionEntry(  35,  19, -1   ), /*                     ID shift  19 */
/* 2065 */  new yyActionEntry( 101, 285, -1   ), /*                     id shift  285 */
/* 2066 */  new yyActionEntry(130,0,-1), /* Unused */
/* 2067 */  new yyActionEntry(  67,  20, 2064 ), /*                 STRING shift  20 */
/* State 285 */
/* 2068 */  new yyActionEntry(  28, 286, -1   ), /*                   FROM shift  286 */
/* State 286 */
/* 2069 */  new yyActionEntry(  35,  19, -1   ), /*                     ID shift  19 */
/* 2070 */  new yyActionEntry( 101, 287, -1   ), /*                     id shift  287 */
/* 2071 */  new yyActionEntry(130,0,-1), /* Unused */
/* 2072 */  new yyActionEntry(  67,  20, 2069 ), /*                 STRING shift  20 */
/* State 287 */
/* 2073 */  new yyActionEntry(  74, 288, -1   ), /*                  USING shift  288 */
/* State 288 */
/* 2074 */  new yyActionEntry(  18, 289, -1   ), /*             DELIMITERS shift  289 */
/* State 289 */
/* 2075 */  new yyActionEntry(  67, 290, -1   ), /*                 STRING shift  290 */
/* State 290 */
/* 2076 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 291 */
/* 2077 */  new yyActionEntry(  35,  19, -1   ), /*                     ID shift  19 */
/* 2078 */  new yyActionEntry( 101, 292, -1   ), /*                     id shift  292 */
/* 2079 */  new yyActionEntry(130,0,-1), /* Unused */
/* 2080 */  new yyActionEntry(  67,  20, 2077 ), /*                 STRING shift  20 */
/* State 292 */
/* 2081 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 293 */
/* 2082 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 294 */
/* 2083 */  new yyActionEntry(130,0,-1), /* Unused */
/* State 295 */
/* 2084 */  new yyActionEntry(  36, 296, -1   ), /*                ILLEGAL shift  296 */
/* State 296 */
/* 2085 */  new yyActionEntry(  65, 297, -1   ), /*                  SPACE shift  297 */
/* State 297 */
/* 2086 */  new yyActionEntry(  70, 298, -1   ), /*        UNCLOSED_STRING shift  298 */
/* State 298 */
/* 2087 */  new yyActionEntry(  11, 299, -1   ), /*                COMMENT shift  299 */
/* State 299 */
/* 2088 */  new yyActionEntry(  29, 300, -1   ), /*               FUNCTION shift  300 */
/* State 300 */
/* 2089 */  new yyActionEntry(  69, 301, -1   ), /*                 UMINUS shift  301 */
/* State 301 */
/* 2090 */  new yyActionEntry(   9, 302, -1   ), /*                 COLUMN shift  302 */
/* State 302 */
/* 2091 */  new yyActionEntry(   1, 303, -1   ), /*           AGG_FUNCTION shift  303 */
/* State 303 */
/* 2092 */  new yyActionEntry(   0, 306, -1   ), /*                      $ reduce 1 */
/* State 304 */
/* 2093 */  new yyActionEntry(130,0,-1), /* Unused */
};

/* The state table contains information needed to look up the correct
** action in the action table, given the current state of the parser.
** Information needed includes:
**
**  +  A pointer to the start of the action hash table in yyActionTable.
**
**  +  A mask used to hash the look-ahead token.  The mask is an integer
**     which is one less than the size of the hash table.
**
**  +  The default action.  This is the action to take if no entry for
**     the given look-ahead is found in the action hash table.
*/
public static class yyStateEntry {
//  public yyActionEntry hashtbl; /* Start of the hash table in yyActionTable */
  public int hashtbl; /* Start of the hash table in yyActionTable */
  public int mask;                      /* Mask used for hashing the look-ahead */
  public int actionDefault;    /* Default action if look-ahead not found */
  public yyStateEntry(int hashtbl,
                      int mask,
                      int actionDefault) {
    this.hashtbl = hashtbl;
    this.mask = mask;
    this.actionDefault = actionDefault;
  }

  public yyActionEntry hashtbl(){
    return yyActionTable[hashtbl];
  }

  public yyActionEntry offset(int offset){
    return yyActionTable[hashtbl + offset];
  }
};
public static yyStateEntry[] yyStateTable = {
  new yyStateEntry( 0, 31, 311 ),
  new yyStateEntry( 32, 1, 469 ),
  new yyStateEntry( 34, 15, 311 ),
  new yyStateEntry( 50, 0, 308 ),
  new yyStateEntry( 51, 15, 469 ),
  new yyStateEntry( 67, 0, 309 ),
  new yyStateEntry( 68, 1, 469 ),
  new yyStateEntry( 70, 0, 313 ),
  new yyStateEntry( 71, 7, 469 ),
  new yyStateEntry( 79, 3, 469 ),
  new yyStateEntry( 83, 0, 469 ),
  new yyStateEntry( 84, 0, 315 ),
  new yyStateEntry( 85, 15, 469 ),
  new yyStateEntry( 101, 0, 316 ),
  new yyStateEntry( 102, 7, 469 ),
  new yyStateEntry( 110, 0, 331 ),
  new yyStateEntry( 111, 7, 318 ),
  new yyStateEntry( 119, 0, 330 ),
  new yyStateEntry( 120, 3, 469 ),
  new yyStateEntry( 124, 0, 320 ),
  new yyStateEntry( 125, 0, 321 ),
  new yyStateEntry( 126, 7, 469 ),
  new yyStateEntry( 134, 0, 332 ),
  new yyStateEntry( 135, 0, 469 ),
  new yyStateEntry( 136, 0, 343 ),
  new yyStateEntry( 137, 0, 469 ),
  new yyStateEntry( 138, 3, 389 ),
  new yyStateEntry( 142, 0, 344 ),
  new yyStateEntry( 143, 0, 387 ),
  new yyStateEntry( 144, 0, 388 ),
  new yyStateEntry( 145, 0, 345 ),
  new yyStateEntry( 146, 0, 469 ),
  new yyStateEntry( 147, 15, 469 ),
  new yyStateEntry( 163, 63, 469 ),
  new yyStateEntry( 227, 15, 457 ),
  new yyStateEntry( 243, 63, 469 ),
  new yyStateEntry( 307, 15, 469 ),
  new yyStateEntry( 323, 0, 360 ),
  new yyStateEntry( 324, 7, 469 ),
  new yyStateEntry( 332, 1, 469 ),
  new yyStateEntry( 334, 0, 361 ),
  new yyStateEntry( 335, 3, 369 ),
  new yyStateEntry( 339, 3, 371 ),
  new yyStateEntry( 343, 3, 469 ),
  new yyStateEntry( 347, 1, 395 ),
  new yyStateEntry( 349, 1, 390 ),
  new yyStateEntry( 351, 1, 392 ),
  new yyStateEntry( 353, 1, 382 ),
  new yyStateEntry( 355, 0, 366 ),
  new yyStateEntry( 356, 0, 469 ),
  new yyStateEntry( 357, 15, 469 ),
  new yyStateEntry( 373, 0, 383 ),
  new yyStateEntry( 374, 15, 469 ),
  new yyStateEntry( 390, 3, 389 ),
  new yyStateEntry( 394, 0, 384 ),
  new yyStateEntry( 395, 31, 386 ),
  new yyStateEntry( 427, 15, 469 ),
  new yyStateEntry( 443, 0, 418 ),
  new yyStateEntry( 444, 0, 469 ),
  new yyStateEntry( 445, 3, 469 ),
  new yyStateEntry( 449, 0, 419 ),
  new yyStateEntry( 450, 0, 420 ),
  new yyStateEntry( 451, 0, 421 ),
  new yyStateEntry( 452, 31, 425 ),
  new yyStateEntry( 484, 15, 469 ),
  new yyStateEntry( 500, 31, 426 ),
  new yyStateEntry( 532, 15, 469 ),
  new yyStateEntry( 548, 7, 427 ),
  new yyStateEntry( 556, 15, 469 ),
  new yyStateEntry( 572, 7, 428 ),
  new yyStateEntry( 580, 15, 469 ),
  new yyStateEntry( 596, 7, 429 ),
  new yyStateEntry( 604, 15, 469 ),
  new yyStateEntry( 620, 7, 430 ),
  new yyStateEntry( 628, 15, 469 ),
  new yyStateEntry( 644, 15, 431 ),
  new yyStateEntry( 660, 15, 469 ),
  new yyStateEntry( 676, 15, 432 ),
  new yyStateEntry( 692, 15, 469 ),
  new yyStateEntry( 708, 15, 433 ),
  new yyStateEntry( 724, 3, 469 ),
  new yyStateEntry( 728, 15, 469 ),
  new yyStateEntry( 744, 31, 434 ),
  new yyStateEntry( 776, 15, 469 ),
  new yyStateEntry( 792, 15, 435 ),
  new yyStateEntry( 808, 15, 469 ),
  new yyStateEntry( 824, 3, 437 ),
  new yyStateEntry( 828, 15, 469 ),
  new yyStateEntry( 844, 3, 438 ),
  new yyStateEntry( 848, 15, 469 ),
  new yyStateEntry( 864, 0, 439 ),
  new yyStateEntry( 865, 15, 469 ),
  new yyStateEntry( 881, 0, 440 ),
  new yyStateEntry( 882, 15, 469 ),
  new yyStateEntry( 898, 0, 441 ),
  new yyStateEntry( 899, 0, 442 ),
  new yyStateEntry( 900, 0, 443 ),
  new yyStateEntry( 901, 15, 469 ),
  new yyStateEntry( 917, 31, 469 ),
  new yyStateEntry( 949, 15, 469 ),
  new yyStateEntry( 965, 63, 469 ),
  new yyStateEntry( 1029, 0, 469 ),
  new yyStateEntry( 1030, 15, 457 ),
  new yyStateEntry( 1046, 7, 469 ),
  new yyStateEntry( 1054, 1, 469 ),
  new yyStateEntry( 1056, 0, 469 ),
  new yyStateEntry( 1057, 0, 469 ),
  new yyStateEntry( 1058, 0, 469 ),
  new yyStateEntry( 1059, 0, 451 ),
  new yyStateEntry( 1060, 31, 456 ),
  new yyStateEntry( 1092, 15, 469 ),
  new yyStateEntry( 1108, 31, 444 ),
  new yyStateEntry( 1140, 15, 469 ),
  new yyStateEntry( 1156, 0, 445 ),
  new yyStateEntry( 1157, 15, 469 ),
  new yyStateEntry( 1173, 0, 446 ),
  new yyStateEntry( 1174, 1, 469 ),
  new yyStateEntry( 1176, 0, 450 ),
  new yyStateEntry( 1177, 15, 457 ),
  new yyStateEntry( 1193, 0, 454 ),
  new yyStateEntry( 1194, 0, 455 ),
  new yyStateEntry( 1195, 15, 469 ),
  new yyStateEntry( 1211, 31, 436 ),
  new yyStateEntry( 1243, 15, 469 ),
  new yyStateEntry( 1259, 31, 469 ),
  new yyStateEntry( 1291, 15, 469 ),
  new yyStateEntry( 1307, 63, 469 ),
  new yyStateEntry( 1371, 0, 469 ),
  new yyStateEntry( 1372, 15, 457 ),
  new yyStateEntry( 1388, 7, 469 ),
  new yyStateEntry( 1396, 0, 453 ),
  new yyStateEntry( 1397, 1, 469 ),
  new yyStateEntry( 1399, 0, 452 ),
  new yyStateEntry( 1400, 3, 389 ),
  new yyStateEntry( 1404, 0, 385 ),
  new yyStateEntry( 1405, 15, 469 ),
  new yyStateEntry( 1421, 31, 393 ),
  new yyStateEntry( 1453, 0, 469 ),
  new yyStateEntry( 1454, 15, 457 ),
  new yyStateEntry( 1470, 0, 391 ),
  new yyStateEntry( 1471, 15, 469 ),
  new yyStateEntry( 1487, 31, 396 ),
  new yyStateEntry( 1519, 0, 370 ),
  new yyStateEntry( 1520, 1, 379 ),
  new yyStateEntry( 1522, 0, 377 ),
  new yyStateEntry( 1523, 0, 378 ),
  new yyStateEntry( 1524, 3, 469 ),
  new yyStateEntry( 1528, 15, 469 ),
  new yyStateEntry( 1544, 0, 376 ),
  new yyStateEntry( 1545, 3, 469 ),
  new yyStateEntry( 1549, 0, 381 ),
  new yyStateEntry( 1550, 0, 372 ),
  new yyStateEntry( 1551, 15, 469 ),
  new yyStateEntry( 1567, 31, 469 ),
  new yyStateEntry( 1599, 3, 469 ),
  new yyStateEntry( 1603, 0, 374 ),
  new yyStateEntry( 1604, 0, 367 ),
  new yyStateEntry( 1605, 0, 368 ),
  new yyStateEntry( 1606, 0, 447 ),
  new yyStateEntry( 1607, 31, 469 ),
  new yyStateEntry( 1639, 0, 416 ),
  new yyStateEntry( 1640, 1, 469 ),
  new yyStateEntry( 1642, 0, 423 ),
  new yyStateEntry( 1643, 0, 469 ),
  new yyStateEntry( 1644, 0, 424 ),
  new yyStateEntry( 1645, 31, 469 ),
  new yyStateEntry( 1677, 0, 346 ),
  new yyStateEntry( 1678, 0, 333 ),
  new yyStateEntry( 1679, 7, 469 ),
  new yyStateEntry( 1687, 0, 334 ),
  new yyStateEntry( 1688, 0, 335 ),
  new yyStateEntry( 1689, 0, 336 ),
  new yyStateEntry( 1690, 1, 469 ),
  new yyStateEntry( 1692, 0, 337 ),
  new yyStateEntry( 1693, 0, 340 ),
  new yyStateEntry( 1694, 1, 469 ),
  new yyStateEntry( 1696, 0, 338 ),
  new yyStateEntry( 1697, 0, 341 ),
  new yyStateEntry( 1698, 0, 339 ),
  new yyStateEntry( 1699, 0, 342 ),
  new yyStateEntry( 1700, 3, 322 ),
  new yyStateEntry( 1704, 3, 469 ),
  new yyStateEntry( 1708, 1, 469 ),
  new yyStateEntry( 1710, 0, 323 ),
  new yyStateEntry( 1711, 3, 469 ),
  new yyStateEntry( 1715, 0, 469 ),
  new yyStateEntry( 1716, 0, 324 ),
  new yyStateEntry( 1717, 0, 327 ),
  new yyStateEntry( 1718, 0, 469 ),
  new yyStateEntry( 1719, 0, 328 ),
  new yyStateEntry( 1720, 0, 469 ),
  new yyStateEntry( 1721, 0, 329 ),
  new yyStateEntry( 1722, 0, 326 ),
  new yyStateEntry( 1723, 0, 325 ),
  new yyStateEntry( 1724, 0, 319 ),
  new yyStateEntry( 1725, 1, 469 ),
  new yyStateEntry( 1727, 7, 469 ),
  new yyStateEntry( 1735, 0, 349 ),
  new yyStateEntry( 1736, 3, 469 ),
  new yyStateEntry( 1740, 3, 469 ),
  new yyStateEntry( 1744, 0, 351 ),
  new yyStateEntry( 1745, 0, 469 ),
  new yyStateEntry( 1746, 0, 469 ),
  new yyStateEntry( 1747, 7, 469 ),
  new yyStateEntry( 1755, 1, 469 ),
  new yyStateEntry( 1757, 0, 353 ),
  new yyStateEntry( 1758, 3, 469 ),
  new yyStateEntry( 1762, 0, 461 ),
  new yyStateEntry( 1763, 0, 463 ),
  new yyStateEntry( 1764, 0, 462 ),
  new yyStateEntry( 1765, 0, 469 ),
  new yyStateEntry( 1766, 3, 469 ),
  new yyStateEntry( 1770, 1, 469 ),
  new yyStateEntry( 1772, 0, 354 ),
  new yyStateEntry( 1773, 3, 469 ),
  new yyStateEntry( 1777, 0, 356 ),
  new yyStateEntry( 1778, 0, 357 ),
  new yyStateEntry( 1779, 15, 469 ),
  new yyStateEntry( 1795, 31, 355 ),
  new yyStateEntry( 1827, 0, 352 ),
  new yyStateEntry( 1828, 0, 350 ),
  new yyStateEntry( 1829, 0, 317 ),
  new yyStateEntry( 1830, 3, 469 ),
  new yyStateEntry( 1834, 3, 469 ),
  new yyStateEntry( 1838, 0, 469 ),
  new yyStateEntry( 1839, 0, 469 ),
  new yyStateEntry( 1840, 3, 469 ),
  new yyStateEntry( 1844, 0, 469 ),
  new yyStateEntry( 1845, 3, 469 ),
  new yyStateEntry( 1849, 0, 469 ),
  new yyStateEntry( 1850, 7, 469 ),
  new yyStateEntry( 1858, 1, 469 ),
  new yyStateEntry( 1860, 0, 458 ),
  new yyStateEntry( 1861, 0, 469 ),
  new yyStateEntry( 1862, 1, 469 ),
  new yyStateEntry( 1864, 3, 469 ),
  new yyStateEntry( 1868, 0, 358 ),
  new yyStateEntry( 1869, 3, 469 ),
  new yyStateEntry( 1873, 0, 464 ),
  new yyStateEntry( 1874, 3, 359 ),
  new yyStateEntry( 1878, 0, 469 ),
  new yyStateEntry( 1879, 3, 469 ),
  new yyStateEntry( 1883, 1, 395 ),
  new yyStateEntry( 1885, 0, 394 ),
  new yyStateEntry( 1886, 3, 469 ),
  new yyStateEntry( 1890, 0, 469 ),
  new yyStateEntry( 1891, 3, 469 ),
  new yyStateEntry( 1895, 3, 395 ),
  new yyStateEntry( 1899, 0, 397 ),
  new yyStateEntry( 1900, 3, 469 ),
  new yyStateEntry( 1904, 0, 469 ),
  new yyStateEntry( 1905, 15, 469 ),
  new yyStateEntry( 1921, 31, 398 ),
  new yyStateEntry( 1953, 0, 469 ),
  new yyStateEntry( 1954, 15, 469 ),
  new yyStateEntry( 1970, 31, 399 ),
  new yyStateEntry( 2002, 0, 469 ),
  new yyStateEntry( 2003, 3, 469 ),
  new yyStateEntry( 2007, 1, 412 ),
  new yyStateEntry( 2009, 3, 469 ),
  new yyStateEntry( 2013, 3, 401 ),
  new yyStateEntry( 2017, 0, 469 ),
  new yyStateEntry( 2018, 7, 469 ),
  new yyStateEntry( 2026, 1, 469 ),
  new yyStateEntry( 2028, 0, 400 ),
  new yyStateEntry( 2029, 7, 469 ),
  new yyStateEntry( 2037, 0, 402 ),
  new yyStateEntry( 2038, 0, 404 ),
  new yyStateEntry( 2039, 1, 469 ),
  new yyStateEntry( 2041, 0, 405 ),
  new yyStateEntry( 2042, 0, 408 ),
  new yyStateEntry( 2043, 1, 469 ),
  new yyStateEntry( 2045, 0, 406 ),
  new yyStateEntry( 2046, 0, 409 ),
  new yyStateEntry( 2047, 0, 407 ),
  new yyStateEntry( 2048, 0, 410 ),
  new yyStateEntry( 2049, 0, 411 ),
  new yyStateEntry( 2050, 0, 403 ),
  new yyStateEntry( 2051, 3, 469 ),
  new yyStateEntry( 2055, 1, 469 ),
  new yyStateEntry( 2057, 0, 413 ),
  new yyStateEntry( 2058, 3, 469 ),
  new yyStateEntry( 2062, 0, 414 ),
  new yyStateEntry( 2063, 0, 415 ),
  new yyStateEntry( 2064, 3, 469 ),
  new yyStateEntry( 2068, 0, 469 ),
  new yyStateEntry( 2069, 3, 469 ),
  new yyStateEntry( 2073, 0, 466 ),
  new yyStateEntry( 2074, 0, 469 ),
  new yyStateEntry( 2075, 0, 469 ),
  new yyStateEntry( 2076, 0, 465 ),
  new yyStateEntry( 2077, 3, 467 ),
  new yyStateEntry( 2081, 0, 468 ),
  new yyStateEntry( 2082, 0, 310 ),
  new yyStateEntry( 2083, 0, 312 ),
  new yyStateEntry( 2084, 0, 469 ),
  new yyStateEntry( 2085, 0, 469 ),
  new yyStateEntry( 2086, 0, 469 ),
  new yyStateEntry( 2087, 0, 469 ),
  new yyStateEntry( 2088, 0, 469 ),
  new yyStateEntry( 2089, 0, 469 ),
  new yyStateEntry( 2090, 0, 469 ),
  new yyStateEntry( 2091, 0, 469 ),
  new yyStateEntry( 2092, 0, 469 ),
  new yyStateEntry( 2093, 0, 307 ),
};

/* The following structure represents a single element of the
** parser's stack.  Information stored includes:
**
**   +  The state number for the parser at this level of the stack.
**
**   +  The value of the token stored at this level of the stack.
**      (In other words, the "major" token.)
**
**   +  The semantic value stored at this level of the stack.  This is
**      the information used by the action routines in the grammar.
**      It is sometimes called the "minor" token.
*/
public static class yyStackEntry {
  public int stateno;       /* The state-number */
  public int major;         /* The major token value.  This is the code
                     ** number for the token at this stack level */
  public YYMINORTYPE minor; /* The user-supplied minor token value.  This
                     ** is the value of the token  */
};

/* The state of the parser is completely contained in an instance of
** the following structure */
public static class yyParser {
  public int idx;                            /* Index of top element in stack */
  public int errcnt;                         /* Shifts left before out of the error */
  public int top;           /* Pointer to the top stack element */
  public yyStackEntry []stack=new yyStackEntry[100];  /* The parser's stack */

  public yyParser() {
    for (int i = 0; i < stack.length; i++) {
      stack[i] = new yyStackEntry();
    }
  }

  public yyStackEntry top() {
    return stack[top];
  }

  public yyStackEntry offset(int offset){
    return stack[top+offset];
  }
};
//typedef struct yyParser yyParser;

//#ifndef NDEBUG
//#include <stdio.h>
public static OutputStream yyTraceFILE = null;
public static String yyTracePrompt = null;

/*
** Turn parser tracing on by giving a stream to which to write the trace
** and a prompt to preface each trace message.  Tracing is turned off
** by making either argument NULL
**
** Inputs:
** <ul>
** <li> A FILE* to which trace output should be written.
**      If NULL, then tracing is turned off.
** <li> A prefix string written at the beginning of every
**      line of trace output.  If NULL, then tracing is
**      turned off.
** </ul>
**
** Outputs:
** None.
*/
/* SQLITE MODIFICATION: Give the function file scope */
public static void ParseTrace(OutputStream TraceFILE, String zTracePrompt){
  yyTraceFILE = TraceFILE;
  yyTracePrompt = zTracePrompt;
  if( yyTraceFILE==null ) yyTracePrompt = null;
  else if( yyTracePrompt==null ) yyTraceFILE = null;
}

/* For tracing shifts, the names of all terminals and nonterminals
** are required.  The following table supplies these names */
public static String[] yyTokenName = {
  "$",             "AGG_FUNCTION",  "ALL",           "AND",         
  "AS",            "ASC",           "BETWEEN",       "BY",          
  "CHECK",         "COLUMN",        "COMMA",         "COMMENT",     
  "CONCAT",        "CONSTRAINT",    "COPY",          "CREATE",      
  "DEFAULT",       "DELETE",        "DELIMITERS",    "DESC",        
  "DISTINCT",      "DOT",           "DROP",          "END_OF_FILE", 
  "EQ",            "EXCEPT",        "EXPLAIN",       "FLOAT",       
  "FROM",          "FUNCTION",      "GE",            "GLOB",        
  "GROUP",         "GT",            "HAVING",        "ID",          
  "ILLEGAL",       "IN",            "INDEX",         "INSERT",      
  "INTEGER",       "INTERSECT",     "INTO",          "IS",          
  "ISNULL",        "KEY",           "LE",            "LIKE",        
  "LP",            "LT",            "MINUS",         "NE",          
  "NOT",           "NOTNULL",       "NULL",          "ON",          
  "OR",            "ORDER",         "PLUS",          "PRIMARY",     
  "RP",            "SELECT",        "SEMI",          "SET",         
  "SLASH",         "SPACE",         "STAR",          "STRING",      
  "TABLE",         "UMINUS",        "UNCLOSED_STRING",  "UNION",       
  "UNIQUE",        "UPDATE",        "USING",         "VACUUM",      
  "VALUES",        "WHERE",         "as",            "carg",        
  "carglist",      "ccons",         "cmd",           "cmdlist",     
  "column",        "columnid",      "columnlist",    "conslist",    
  "conslist_opt",  "create_table",  "create_table_args",  "distinct",    
  "ecmd",          "error",         "explain",       "expr",        
  "expritem",      "exprlist",      "from",          "groupby_opt", 
  "having_opt",    "id",            "idlist",        "idxitem",     
  "idxlist",       "input",         "inscollist",    "inscollist_opt",
  "item",          "itemlist",      "joinop",        "oneselect",   
  "orderby_opt",   "sclp",          "selcollist",    "select",      
  "seltablist",    "setlist",       "signed",        "sortitem",    
  "sortlist",      "sortorder",     "stl_prefix",    "tcons",       
  "tcons2",        "type",          "typename",      "uniqueflag",  
  "where_opt",   
};

public static final int  TK_AGG_FUNCTION                   =  1;
public static final int  TK_ALL                            =  2;
public static final int  TK_AND                            =  3;
public static final int  TK_AS                             =  4;
public static final int  TK_ASC                            =  5;
public static final int  TK_BETWEEN                        =  6;
public static final int  TK_BY                             =  7;
public static final int  TK_CHECK                          =  8;
public static final int  TK_COLUMN                         =  9;
public static final int  TK_COMMA                          = 10;
public static final int  TK_COMMENT                        = 11;
public static final int  TK_CONCAT                         = 12;
public static final int  TK_CONSTRAINT                     = 13;
public static final int  TK_COPY                           = 14;
public static final int  TK_CREATE                         = 15;
public static final int  TK_DEFAULT                        = 16;
public static final int  TK_DELETE                         = 17;
public static final int  TK_DELIMITERS                     = 18;
public static final int  TK_DESC                           = 19;
public static final int  TK_DISTINCT                       = 20;
public static final int  TK_DOT                            = 21;
public static final int  TK_DROP                           = 22;
public static final int  TK_END_OF_FILE                    = 23;
public static final int  TK_EQ                             = 24;
public static final int  TK_EXCEPT                         = 25;
public static final int  TK_EXPLAIN                        = 26;
public static final int  TK_FLOAT                          = 27;
public static final int  TK_FROM                           = 28;
public static final int  TK_FUNCTION                       = 29;
public static final int  TK_GE                             = 30;
public static final int  TK_GLOB                           = 31;
public static final int  TK_GROUP                          = 32;
public static final int  TK_GT                             = 33;
public static final int  TK_HAVING                         = 34;
public static final int  TK_ID                             = 35;
public static final int  TK_ILLEGAL                        = 36;
public static final int  TK_IN                             = 37;
public static final int  TK_INDEX                          = 38;
public static final int  TK_INSERT                         = 39;
public static final int  TK_INTEGER                        = 40;
public static final int  TK_INTERSECT                      = 41;
public static final int  TK_INTO                           = 42;
public static final int  TK_IS                             = 43;
public static final int  TK_ISNULL                         = 44;
public static final int  TK_KEY                            = 45;
public static final int  TK_LE                             = 46;
public static final int  TK_LIKE                           = 47;
public static final int  TK_LP                             = 48;
public static final int  TK_LT                             = 49;
public static final int  TK_MINUS                          = 50;
public static final int  TK_NE                             = 51;
public static final int  TK_NOT                            = 52;
public static final int  TK_NOTNULL                        = 53;
public static final int  TK_NULL                           = 54;
public static final int  TK_ON                             = 55;
public static final int  TK_OR                             = 56;
public static final int  TK_ORDER                          = 57;
public static final int  TK_PLUS                           = 58;
public static final int  TK_PRIMARY                        = 59;
public static final int  TK_RP                             = 60;
public static final int  TK_SELECT                         = 61;
public static final int  TK_SEMI                           = 62;
public static final int  TK_SET                            = 63;
public static final int  TK_SLASH                          = 64;
public static final int  TK_SPACE                          = 65;
public static final int  TK_STAR                           = 66;
public static final int  TK_STRING                         = 67;
public static final int  TK_TABLE                          = 68;
public static final int  TK_UMINUS                         = 69;
public static final int  TK_UNCLOSED_STRING                = 70;
public static final int  TK_UNION                          = 71;
public static final int  TK_UNIQUE                         = 72;
public static final int  TK_UPDATE                         = 73;
public static final int  TK_USING                          = 74;
public static final int  TK_VACUUM                         = 75;
public static final int  TK_VALUES                         = 76;
public static final int  TK_WHERE                          = 77;

public static void trace(byte[] bs){
  try{
    if(yyTraceFILE != null){
      yyTraceFILE.write(bs);
    }
  } catch (IOException e) {
    e.printStackTrace();
  }
}

public static void YYTRACE(Object X){
  trace(String.format("%sReduce [%s].\n",yyTracePrompt,X).getBytes());
}
//#define YYTRACE(X) if( yyTraceFILE ) yyTraceFILE.write(String.format("%sReduce [%s].\n",yyTracePrompt,X);
//#else
//#define YYTRACE(X)
//#endif

/*
** This function allocates a new parser.
** The only argument is a pointer to a function which works like
** malloc.
**
** Inputs:
** A pointer to the function used to allocate memory.
**
** Outputs:
** A pointer to a parser.  This pointer is used in subsequent calls
** to Parse and ParseFree.
*/
/* SQLITE MODIFICATION: Give the function file scope */
public static yyParser ParseAlloc(){
  yyParser ret = new yyParser();
  ret.idx = -1;
  return ret;
//  yyParser *pParser;
//  pParser = (yyParser*)(*mallocProc)( sizeof(yyParser), __FILE__, __LINE__ );
//  if( pParser ){
//    pParser.idx = -1;
//  }
//  return pParser;
}

/* The following function deletes the value associated with a
** symbol.  The symbol can be either a terminal or nonterminal.
** "yymajor" is the symbol code, and "yypminor" is a pointer to
** the value.
*/
public static void yy_destructor(int yymajor, YYMINORTYPE yypminor){
  switch( yymajor ){
    /* Here is inserted the actions which take place when a
    ** terminal or non-terminal is destroyed.  This can happen
    ** when the symbol is popped from the stack during a
    ** reduce or during error processing or when a parser is
    ** being destroyed before it is finished parsing.
    **
    ** Note: during a reduce, the only symbols destroyed are those
    ** which appear on the RHS of the rule, but which are not used
    ** inside the C code.
    */
    case 95:
// #line 313 "D:/workspace/java/sqlite/work/parse.y"
{sqliteExprDelete((yypminor.yy234));}
// #line 3062 "D:/workspace/java/sqlite/work/parse.java"
      break;
    case 96:
// #line 427 "D:/workspace/java/sqlite/work/parse.y"
{sqliteExprDelete((yypminor.yy234));}
// #line 3067 "D:/workspace/java/sqlite/work/parse.java"
      break;
    case 97:
// #line 425 "D:/workspace/java/sqlite/work/parse.y"
{sqliteExprListDelete((yypminor.yy154));}
// #line 3072 "D:/workspace/java/sqlite/work/parse.java"
      break;
    case 98:
// #line 199 "D:/workspace/java/sqlite/work/parse.y"
{sqliteIdListDelete((yypminor.yy204));}
// #line 3077 "D:/workspace/java/sqlite/work/parse.java"
      break;
    case 99:
// #line 235 "D:/workspace/java/sqlite/work/parse.y"
{sqliteExprListDelete((yypminor.yy154));}
// #line 3082 "D:/workspace/java/sqlite/work/parse.java"
      break;
    case 100:
// #line 240 "D:/workspace/java/sqlite/work/parse.y"
{sqliteExprDelete((yypminor.yy234));}
// #line 3087 "D:/workspace/java/sqlite/work/parse.java"
      break;
    case 104:
// #line 442 "D:/workspace/java/sqlite/work/parse.y"
{sqliteIdListDelete((yypminor.yy204));}
// #line 3092 "D:/workspace/java/sqlite/work/parse.java"
      break;
    case 106:
// #line 295 "D:/workspace/java/sqlite/work/parse.y"
{sqliteIdListDelete((yypminor.yy204));}
// #line 3097 "D:/workspace/java/sqlite/work/parse.java"
      break;
    case 107:
// #line 293 "D:/workspace/java/sqlite/work/parse.y"
{sqliteIdListDelete((yypminor.yy204));}
// #line 3102 "D:/workspace/java/sqlite/work/parse.java"
      break;
    case 108:
// #line 273 "D:/workspace/java/sqlite/work/parse.y"
{sqliteExprDelete((yypminor.yy234));}
// #line 3107 "D:/workspace/java/sqlite/work/parse.java"
      break;
    case 109:
// #line 271 "D:/workspace/java/sqlite/work/parse.y"
{sqliteExprListDelete((yypminor.yy154));}
// #line 3112 "D:/workspace/java/sqlite/work/parse.java"
      break;
    case 111:
// #line 151 "D:/workspace/java/sqlite/work/parse.y"
{sqliteSelectDelete((yypminor.yy39));}
// #line 3117 "D:/workspace/java/sqlite/work/parse.java"
      break;
    case 112:
// #line 210 "D:/workspace/java/sqlite/work/parse.y"
{sqliteExprListDelete((yypminor.yy154));}
// #line 3122 "D:/workspace/java/sqlite/work/parse.java"
      break;
    case 113:
// #line 184 "D:/workspace/java/sqlite/work/parse.y"
{sqliteExprListDelete((yypminor.yy154));}
// #line 3127 "D:/workspace/java/sqlite/work/parse.java"
      break;
    case 114:
// #line 182 "D:/workspace/java/sqlite/work/parse.y"
{sqliteExprListDelete((yypminor.yy154));}
// #line 3132 "D:/workspace/java/sqlite/work/parse.java"
      break;
    case 115:
// #line 149 "D:/workspace/java/sqlite/work/parse.y"
{sqliteSelectDelete((yypminor.yy39));}
// #line 3137 "D:/workspace/java/sqlite/work/parse.java"
      break;
    case 116:
// #line 195 "D:/workspace/java/sqlite/work/parse.y"
{sqliteIdListDelete((yypminor.yy204));}
// #line 3142 "D:/workspace/java/sqlite/work/parse.java"
      break;
    case 117:
// #line 255 "D:/workspace/java/sqlite/work/parse.y"
{sqliteExprListDelete((yypminor.yy154));}
// #line 3147 "D:/workspace/java/sqlite/work/parse.java"
      break;
    case 119:
// #line 214 "D:/workspace/java/sqlite/work/parse.y"
{sqliteExprDelete((yypminor.yy234));}
// #line 3152 "D:/workspace/java/sqlite/work/parse.java"
      break;
    case 120:
// #line 212 "D:/workspace/java/sqlite/work/parse.y"
{sqliteExprListDelete((yypminor.yy154));}
// #line 3157 "D:/workspace/java/sqlite/work/parse.java"
      break;
    case 122:
// #line 197 "D:/workspace/java/sqlite/work/parse.y"
{sqliteIdListDelete((yypminor.yy204));}
// #line 3162 "D:/workspace/java/sqlite/work/parse.java"
      break;
    case 128:
// #line 249 "D:/workspace/java/sqlite/work/parse.y"
{sqliteExprDelete((yypminor.yy234));}
// #line 3167 "D:/workspace/java/sqlite/work/parse.java"
      break;
    default:  break;   /* If no destructor action specified: do nothing */
  }
}

/*
** Pop the parser's stack once.
**
** If there is a destructor routine associated with the token which
** is popped from the stack, then call it.
**
** Return the major token number for the symbol popped.
*/
public static int yy_pop_parser_stack(yyParser pParser){
        int yymajor;

  if( pParser.idx<0 ) return 0;
//#ifndef NDEBUG
  if( yyTraceFILE!=null && pParser.idx>=0 ){
    trace(String.format("%sPopping %s\n",
      yyTracePrompt,
      yyTokenName[pParser.top().major]).getBytes());
  }
//#endif
  yymajor = pParser.top().major;
  yy_destructor( yymajor, pParser.top().minor);
  pParser.idx--;
  pParser.top--;
  return yymajor;
}

/*
** Deallocate and destroy a parser.  Destructors are all called for
** all stack elements before shutting the parser down.
**
** Inputs:
** <ul>
** <li>  A pointer to the parser.  This should be a pointer
**       obtained from ParseAlloc.
** <li>  A pointer to a function used to reclaim memory obtained
**       from malloc.
** </ul>
*/
/* SQLITE MODIFICATION: Give the function file scope */
//void ParseFree(
//  void *p,               /* The parser to be deleted */
//  void (*freeProc)()     /* Function used to reclaim memory */
//){
//  yyParser *pParser = (yyParser*)p;
//  if( pParser==0 ) return;
//  while( pParser.idx>=0 ) yy_pop_parser_stack(pParser);
//  (*freeProc)(pParser, __FILE__, __LINE__);
//}

/*
** Find the appropriate action for a parser given the look-ahead token.
**
** If the look-ahead token is 130, then check to see if the action is
** independent of the look-ahead.  If it is, return the action, otherwise
** return YY_NO_ACTION.
*/
static int yy_find_parser_action(
  yyParser pParser,        /* The parser */
  int iLookAhead             /* The look-ahead token */
){
  yyStateEntry pState;   /* Appropriate entry in the state table */
  yyActionEntry pAction; /* Action appropriate for the look-ahead */

  /* if( pParser.idx<0 ) return YY_NO_ACTION;  */
  int stateNo = pParser.top().stateno;
  pState = yyStateTable[stateNo];
  if( iLookAhead!=130 ){
    int offset = pState.hashtbl + (iLookAhead & pState.mask);
    pAction = pState.offset(iLookAhead & pState.mask);
    while( pAction!= null ){
      if( pAction.lookahead==iLookAhead ) return pAction.action;
      pAction = pAction.next();
    }
  }else if( pState.mask!=0 || pState.hashtbl().lookahead!=130 ){
    return YY_NO_ACTION;
  }
  return pState.actionDefault;
}

/*
** Perform a shift action.
*/
static void yy_shift(
  yyParser yypParser,          /* The parser to be shifted */
  int yyNewState,               /* The new state to shift in */
  int yyMajor,                  /* The major token to shift in */
  YYMINORTYPE yypMinor         /* Pointer ot the minor token to shift in */
){
  yypParser.idx++;
  yypParser.top++;
  if( yypParser.idx>=100 ){
     yypParser.idx--;
     yypParser.top--;
//#ifndef NDEBUG
     if( yyTraceFILE != null){
       trace(String.format("%sStack Overflow!\n",yyTracePrompt).getBytes());
     }
//#endif
     while( yypParser.idx>=0 ) yy_pop_parser_stack(yypParser);
     /* Here code is inserted which will execute if the parser
     ** stack every overflows */
     return;
  }
  yypParser.top().stateno = yyNewState;
  yypParser.top().major = yyMajor;
  yypParser.top().minor = yypMinor;
//#ifndef NDEBUG
  if( yyTraceFILE != null && yypParser.idx>0 ){
    int i;
    trace(String.format("%sShift %d\n",yyTracePrompt,yyNewState).getBytes());
    trace(String.format("%sStack:",yyTracePrompt).getBytes());
    for(i=1; i<=yypParser.idx; i++)
      trace(String.format(" %s",yyTokenName[yypParser.stack[i].major]).getBytes());
    trace(String.format("\n").getBytes());
  }
//#endif
}

/* The following table contains information about every rule that
** is used during the reduce.
*/
public static class yyRuleInfo {
  public int lhs;         /* Symbol on the left-hand side of the rule */
  public int nrhs;     /* Number of right-hand side symbols in the rule */
  public yyRuleInfo(int lhs, int nrhs){
    this.lhs = lhs;
    this.nrhs = nrhs;
  }
}
public static  yyRuleInfo[] yyRuleInfo= {
  new yyRuleInfo( 105, 1 ),
  new yyRuleInfo( 105, 9 ),
  new yyRuleInfo( 83, 1 ),
  new yyRuleInfo( 83, 3 ),
  new yyRuleInfo( 92, 2 ),
  new yyRuleInfo( 92, 1 ),
  new yyRuleInfo( 92, 0 ),
  new yyRuleInfo( 94, 1 ),
  new yyRuleInfo( 82, 2 ),
  new yyRuleInfo( 89, 3 ),
  new yyRuleInfo( 90, 4 ),
  new yyRuleInfo( 86, 3 ),
  new yyRuleInfo( 86, 1 ),
  new yyRuleInfo( 84, 3 ),
  new yyRuleInfo( 85, 1 ),
  new yyRuleInfo( 101, 1 ),
  new yyRuleInfo( 101, 1 ),
  new yyRuleInfo( 125, 1 ),
  new yyRuleInfo( 125, 4 ),
  new yyRuleInfo( 125, 6 ),
  new yyRuleInfo( 126, 1 ),
  new yyRuleInfo( 126, 2 ),
  new yyRuleInfo( 118, 1 ),
  new yyRuleInfo( 118, 2 ),
  new yyRuleInfo( 118, 2 ),
  new yyRuleInfo( 80, 2 ),
  new yyRuleInfo( 80, 0 ),
  new yyRuleInfo( 79, 3 ),
  new yyRuleInfo( 79, 1 ),
  new yyRuleInfo( 79, 2 ),
  new yyRuleInfo( 79, 2 ),
  new yyRuleInfo( 79, 2 ),
  new yyRuleInfo( 79, 3 ),
  new yyRuleInfo( 79, 3 ),
  new yyRuleInfo( 79, 2 ),
  new yyRuleInfo( 79, 3 ),
  new yyRuleInfo( 79, 3 ),
  new yyRuleInfo( 79, 2 ),
  new yyRuleInfo( 81, 2 ),
  new yyRuleInfo( 81, 3 ),
  new yyRuleInfo( 81, 1 ),
  new yyRuleInfo( 81, 4 ),
  new yyRuleInfo( 88, 0 ),
  new yyRuleInfo( 88, 2 ),
  new yyRuleInfo( 87, 3 ),
  new yyRuleInfo( 87, 1 ),
  new yyRuleInfo( 123, 3 ),
  new yyRuleInfo( 123, 1 ),
  new yyRuleInfo( 124, 5 ),
  new yyRuleInfo( 124, 4 ),
  new yyRuleInfo( 124, 2 ),
  new yyRuleInfo( 102, 3 ),
  new yyRuleInfo( 102, 1 ),
  new yyRuleInfo( 82, 3 ),
  new yyRuleInfo( 82, 1 ),
  new yyRuleInfo( 115, 1 ),
  new yyRuleInfo( 115, 3 ),
  new yyRuleInfo( 110, 1 ),
  new yyRuleInfo( 110, 2 ),
  new yyRuleInfo( 110, 1 ),
  new yyRuleInfo( 110, 1 ),
  new yyRuleInfo( 111, 8 ),
  new yyRuleInfo( 91, 1 ),
  new yyRuleInfo( 91, 1 ),
  new yyRuleInfo( 91, 0 ),
  new yyRuleInfo( 113, 2 ),
  new yyRuleInfo( 113, 0 ),
  new yyRuleInfo( 114, 1 ),
  new yyRuleInfo( 114, 2 ),
  new yyRuleInfo( 114, 4 ),
  new yyRuleInfo( 78, 0 ),
  new yyRuleInfo( 78, 1 ),
  new yyRuleInfo( 98, 2 ),
  new yyRuleInfo( 122, 2 ),
  new yyRuleInfo( 122, 0 ),
  new yyRuleInfo( 116, 2 ),
  new yyRuleInfo( 116, 4 ),
  new yyRuleInfo( 112, 0 ),
  new yyRuleInfo( 112, 3 ),
  new yyRuleInfo( 120, 4 ),
  new yyRuleInfo( 120, 2 ),
  new yyRuleInfo( 119, 1 ),
  new yyRuleInfo( 121, 1 ),
  new yyRuleInfo( 121, 1 ),
  new yyRuleInfo( 121, 0 ),
  new yyRuleInfo( 99, 0 ),
  new yyRuleInfo( 99, 3 ),
  new yyRuleInfo( 100, 0 ),
  new yyRuleInfo( 100, 2 ),
  new yyRuleInfo( 82, 4 ),
  new yyRuleInfo( 128, 0 ),
  new yyRuleInfo( 128, 2 ),
  new yyRuleInfo( 82, 5 ),
  new yyRuleInfo( 117, 5 ),
  new yyRuleInfo( 117, 3 ),
  new yyRuleInfo( 82, 8 ),
  new yyRuleInfo( 82, 5 ),
  new yyRuleInfo( 109, 3 ),
  new yyRuleInfo( 109, 1 ),
  new yyRuleInfo( 108, 1 ),
  new yyRuleInfo( 108, 2 ),
  new yyRuleInfo( 108, 2 ),
  new yyRuleInfo( 108, 1 ),
  new yyRuleInfo( 108, 2 ),
  new yyRuleInfo( 108, 2 ),
  new yyRuleInfo( 108, 1 ),
  new yyRuleInfo( 108, 1 ),
  new yyRuleInfo( 107, 0 ),
  new yyRuleInfo( 107, 3 ),
  new yyRuleInfo( 106, 3 ),
  new yyRuleInfo( 106, 1 ),
  new yyRuleInfo( 95, 3 ),
  new yyRuleInfo( 95, 1 ),
  new yyRuleInfo( 95, 1 ),
  new yyRuleInfo( 95, 3 ),
  new yyRuleInfo( 95, 1 ),
  new yyRuleInfo( 95, 1 ),
  new yyRuleInfo( 95, 1 ),
  new yyRuleInfo( 95, 4 ),
  new yyRuleInfo( 95, 4 ),
  new yyRuleInfo( 95, 3 ),
  new yyRuleInfo( 95, 3 ),
  new yyRuleInfo( 95, 3 ),
  new yyRuleInfo( 95, 3 ),
  new yyRuleInfo( 95, 3 ),
  new yyRuleInfo( 95, 3 ),
  new yyRuleInfo( 95, 3 ),
  new yyRuleInfo( 95, 3 ),
  new yyRuleInfo( 95, 3 ),
  new yyRuleInfo( 95, 4 ),
  new yyRuleInfo( 95, 3 ),
  new yyRuleInfo( 95, 4 ),
  new yyRuleInfo( 95, 3 ),
  new yyRuleInfo( 95, 3 ),
  new yyRuleInfo( 95, 3 ),
  new yyRuleInfo( 95, 3 ),
  new yyRuleInfo( 95, 3 ),
  new yyRuleInfo( 95, 2 ),
  new yyRuleInfo( 95, 2 ),
  new yyRuleInfo( 95, 2 ),
  new yyRuleInfo( 95, 2 ),
  new yyRuleInfo( 95, 2 ),
  new yyRuleInfo( 95, 3 ),
  new yyRuleInfo( 95, 5 ),
  new yyRuleInfo( 95, 6 ),
  new yyRuleInfo( 95, 5 ),
  new yyRuleInfo( 95, 5 ),
  new yyRuleInfo( 95, 6 ),
  new yyRuleInfo( 95, 6 ),
  new yyRuleInfo( 97, 3 ),
  new yyRuleInfo( 97, 1 ),
  new yyRuleInfo( 96, 1 ),
  new yyRuleInfo( 96, 0 ),
  new yyRuleInfo( 82, 9 ),
  new yyRuleInfo( 127, 1 ),
  new yyRuleInfo( 127, 0 ),
  new yyRuleInfo( 104, 3 ),
  new yyRuleInfo( 104, 1 ),
  new yyRuleInfo( 103, 1 ),
  new yyRuleInfo( 82, 3 ),
  new yyRuleInfo( 82, 7 ),
  new yyRuleInfo( 82, 4 ),
  new yyRuleInfo( 82, 1 ),
  new yyRuleInfo( 82, 2 ),
};

// static void yy_accept();  /* Forward declaration */

/*
** Perform a reduce action and the shift that must immediately
** follow the reduce.
*/
static void yy_reduce(
  yyParser yypParser,         /* The parser */
  int yyruleno                 /* Number of the rule by which to reduce */
  ,Parse pParse
){
  int yygoto;                     /* The next state */
  int yyact;                      /* The next action */
  YYMINORTYPE yygotominor = new YYMINORTYPE();        /* The LHS of the rule reduced */
  yyStackEntry yymsp;     /* The top of the parser's stack */
  int yysize;                     /* Amount to pop the stack */
  yymsp = yypParser.top();
  switch( yyruleno ){
  /* Beginning here are the reduction cases.  A typical example
  ** follows:
  **   case 0:
  **     YYTRACE("<text of the rule>");
  **  #line <lineno> <grammarfile>
  **     { ... }           // User supplied code
  **  #line <lineno> <thisfile>
  **     break;
  */
      case 0:
        YYTRACE("input ::= cmdlist");
        /* No destructor defined for cmdlist */
        break;
      case 1:
        YYTRACE("input ::= END_OF_FILE ILLEGAL SPACE UNCLOSED_STRING COMMENT FUNCTION UMINUS COLUMN AGG_FUNCTION");
        /* No destructor defined for END_OF_FILE */
        /* No destructor defined for ILLEGAL */
        /* No destructor defined for SPACE */
        /* No destructor defined for UNCLOSED_STRING */
        /* No destructor defined for COMMENT */
        /* No destructor defined for FUNCTION */
        /* No destructor defined for UMINUS */
        /* No destructor defined for COLUMN */
        /* No destructor defined for AGG_FUNCTION */
        break;
      case 2:
        YYTRACE("cmdlist ::= ecmd");
        /* No destructor defined for ecmd */
        break;
      case 3:
        YYTRACE("cmdlist ::= cmdlist SEMI ecmd");
        /* No destructor defined for cmdlist */
        /* No destructor defined for SEMI */
        /* No destructor defined for ecmd */
        break;
      case 4:
        YYTRACE("ecmd ::= explain cmd");
// #line 70 "D:/workspace/java/sqlite/work/parse.y"
{sqliteExec(pParse);}
// #line 3526 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for explain */
        /* No destructor defined for cmd */
        break;
      case 5:
        YYTRACE("ecmd ::= cmd");
// #line 71 "D:/workspace/java/sqlite/work/parse.y"
{sqliteExec(pParse);}
// #line 3534 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for cmd */
        break;
      case 6:
        YYTRACE("ecmd ::=");
        break;
      case 7:
        YYTRACE("explain ::= EXPLAIN");
// #line 73 "D:/workspace/java/sqlite/work/parse.y"
{pParse.explain = 1;}
// #line 3544 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for EXPLAIN */
        break;
      case 8:
        YYTRACE("cmd ::= create_table create_table_args");
        /* No destructor defined for create_table */
        /* No destructor defined for create_table_args */
        break;
      case 9:
        YYTRACE("create_table ::= CREATE TABLE id");
// #line 78 "D:/workspace/java/sqlite/work/parse.y"
{sqliteStartTable(pParse,yypParser.offset(-2).minor.yy0,yypParser.offset(0).minor.yy180);}
// #line 3556 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for TABLE */
        break;
      case 10:
        YYTRACE("create_table_args ::= LP columnlist conslist_opt RP");
// #line 80 "D:/workspace/java/sqlite/work/parse.y"
{sqliteEndTable(pParse,yypParser.offset(0).minor.yy0);}
// #line 3563 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for LP */
        /* No destructor defined for columnlist */
        /* No destructor defined for conslist_opt */
        break;
      case 11:
        YYTRACE("columnlist ::= columnlist COMMA column");
        /* No destructor defined for columnlist */
        /* No destructor defined for COMMA */
        /* No destructor defined for column */
        break;
      case 12:
        YYTRACE("columnlist ::= column");
        /* No destructor defined for column */
        break;
      case 13:
        YYTRACE("column ::= columnid type carglist");
        /* No destructor defined for columnid */
        /* No destructor defined for type */
        /* No destructor defined for carglist */
        break;
      case 14:
        YYTRACE("columnid ::= id");
// #line 89 "D:/workspace/java/sqlite/work/parse.y"
{sqliteAddColumn(pParse,yypParser.offset(0).minor.yy180);}
// #line 3588 "D:/workspace/java/sqlite/work/parse.java"
        break;
      case 15:
        YYTRACE("id ::= ID");
// #line 91 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy180 = yypParser.offset(0).minor.yy0;}
// #line 3594 "D:/workspace/java/sqlite/work/parse.java"
        break;
      case 16:
        YYTRACE("id ::= STRING");
// #line 92 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy180 = yypParser.offset(0).minor.yy0;}
// #line 3600 "D:/workspace/java/sqlite/work/parse.java"
        break;
      case 17:
        YYTRACE("type ::= typename");
        /* No destructor defined for typename */
        break;
      case 18:
        YYTRACE("type ::= typename LP signed RP");
        /* No destructor defined for typename */
        /* No destructor defined for LP */
        /* No destructor defined for signed */
        /* No destructor defined for RP */
        break;
      case 19:
        YYTRACE("type ::= typename LP signed COMMA signed RP");
        /* No destructor defined for typename */
        /* No destructor defined for LP */
        /* No destructor defined for signed */
        /* No destructor defined for COMMA */
        /* No destructor defined for signed */
        /* No destructor defined for RP */
        break;
      case 20:
        YYTRACE("typename ::= id");
        /* No destructor defined for id */
        break;
      case 21:
        YYTRACE("typename ::= typename id");
        /* No destructor defined for typename */
        /* No destructor defined for id */
        break;
      case 22:
        YYTRACE("signed ::= INTEGER");
        /* No destructor defined for INTEGER */
        break;
      case 23:
        YYTRACE("signed ::= PLUS INTEGER");
        /* No destructor defined for PLUS */
        /* No destructor defined for INTEGER */
        break;
      case 24:
        YYTRACE("signed ::= MINUS INTEGER");
        /* No destructor defined for MINUS */
        /* No destructor defined for INTEGER */
        break;
      case 25:
        YYTRACE("carglist ::= carglist carg");
        /* No destructor defined for carglist */
        /* No destructor defined for carg */
        break;
      case 26:
        YYTRACE("carglist ::=");
        break;
      case 27:
        YYTRACE("carg ::= CONSTRAINT id ccons");
        /* No destructor defined for CONSTRAINT */
        /* No destructor defined for id */
        /* No destructor defined for ccons */
        break;
      case 28:
        YYTRACE("carg ::= ccons");
        /* No destructor defined for ccons */
        break;
      case 29:
        YYTRACE("carg ::= DEFAULT STRING");
// #line 105 "D:/workspace/java/sqlite/work/parse.y"
{sqliteAddDefaultValue(pParse,yypParser.offset(0).minor.yy0,0);}
// #line 3667 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for DEFAULT */
        break;
      case 30:
        YYTRACE("carg ::= DEFAULT ID");
// #line 106 "D:/workspace/java/sqlite/work/parse.y"
{sqliteAddDefaultValue(pParse,yypParser.offset(0).minor.yy0,0);}
// #line 3674 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for DEFAULT */
        break;
      case 31:
        YYTRACE("carg ::= DEFAULT INTEGER");
// #line 107 "D:/workspace/java/sqlite/work/parse.y"
{sqliteAddDefaultValue(pParse,yypParser.offset(0).minor.yy0,0);}
// #line 3681 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for DEFAULT */
        break;
      case 32:
        YYTRACE("carg ::= DEFAULT PLUS INTEGER");
// #line 108 "D:/workspace/java/sqlite/work/parse.y"
{sqliteAddDefaultValue(pParse,yypParser.offset(0).minor.yy0,0);}
// #line 3688 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for DEFAULT */
        /* No destructor defined for PLUS */
        break;
      case 33:
        YYTRACE("carg ::= DEFAULT MINUS INTEGER");
// #line 109 "D:/workspace/java/sqlite/work/parse.y"
{sqliteAddDefaultValue(pParse,yypParser.offset(0).minor.yy0,1);}
// #line 3696 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for DEFAULT */
        /* No destructor defined for MINUS */
        break;
      case 34:
        YYTRACE("carg ::= DEFAULT FLOAT");
// #line 110 "D:/workspace/java/sqlite/work/parse.y"
{sqliteAddDefaultValue(pParse,yypParser.offset(0).minor.yy0,0);}
// #line 3704 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for DEFAULT */
        break;
      case 35:
        YYTRACE("carg ::= DEFAULT PLUS FLOAT");
// #line 111 "D:/workspace/java/sqlite/work/parse.y"
{sqliteAddDefaultValue(pParse,yypParser.offset(0).minor.yy0,0);}
// #line 3711 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for DEFAULT */
        /* No destructor defined for PLUS */
        break;
      case 36:
        YYTRACE("carg ::= DEFAULT MINUS FLOAT");
// #line 112 "D:/workspace/java/sqlite/work/parse.y"
{sqliteAddDefaultValue(pParse,yypParser.offset(0).minor.yy0,1);}
// #line 3719 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for DEFAULT */
        /* No destructor defined for MINUS */
        break;
      case 37:
        YYTRACE("carg ::= DEFAULT NULL");
        /* No destructor defined for DEFAULT */
        /* No destructor defined for NULL */
        break;
      case 38:
        YYTRACE("ccons ::= NOT NULL");
        /* No destructor defined for NOT */
        /* No destructor defined for NULL */
        break;
      case 39:
        YYTRACE("ccons ::= PRIMARY KEY sortorder");
// #line 118 "D:/workspace/java/sqlite/work/parse.y"
{sqliteCreateIndex(pParse,0,0,0,0,0);}
// #line 3737 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for PRIMARY */
        /* No destructor defined for KEY */
        /* No destructor defined for sortorder */
        break;
      case 40:
        YYTRACE("ccons ::= UNIQUE");
        /* No destructor defined for UNIQUE */
        break;
      case 41:
        YYTRACE("ccons ::= CHECK LP expr RP");
        /* No destructor defined for CHECK */
        /* No destructor defined for LP */
//        yy_destructor(95,&yymsp[-1].minor);
        /* No destructor defined for RP */
        break;
      case 42:
        YYTRACE("conslist_opt ::=");
        break;
      case 43:
        YYTRACE("conslist_opt ::= COMMA conslist");
        /* No destructor defined for COMMA */
        /* No destructor defined for conslist */
        break;
      case 44:
        YYTRACE("conslist ::= conslist COMMA tcons");
        /* No destructor defined for conslist */
        /* No destructor defined for COMMA */
        /* No destructor defined for tcons */
        break;
      case 45:
        YYTRACE("conslist ::= tcons");
        /* No destructor defined for tcons */
        break;
      case 46:
        YYTRACE("tcons ::= CONSTRAINT id tcons2");
        /* No destructor defined for CONSTRAINT */
        /* No destructor defined for id */
        /* No destructor defined for tcons2 */
        break;
      case 47:
        YYTRACE("tcons ::= tcons2");
        /* No destructor defined for tcons2 */
        break;
      case 48:
        YYTRACE("tcons2 ::= PRIMARY KEY LP idxlist RP");
// #line 131 "D:/workspace/java/sqlite/work/parse.y"
{sqliteCreateIndex(pParse,0,0,yypParser.offset(-1).minor.yy204,0,0);}
// #line 3785 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for PRIMARY */
        /* No destructor defined for KEY */
        /* No destructor defined for LP */
        /* No destructor defined for RP */
        break;
      case 49:
        YYTRACE("tcons2 ::= UNIQUE LP idlist RP");
        /* No destructor defined for UNIQUE */
        /* No destructor defined for LP */
        /* No destructor defined for idlist */
        /* No destructor defined for RP */
        break;
      case 50:
        YYTRACE("tcons2 ::= CHECK expr");
        /* No destructor defined for CHECK */
//        yy_destructor(95,&yymsp[0].minor);
        break;
      case 51:
        YYTRACE("idlist ::= idlist COMMA id");
        /* No destructor defined for idlist */
        /* No destructor defined for COMMA */
        /* No destructor defined for id */
        break;
      case 52:
        YYTRACE("idlist ::= id");
        /* No destructor defined for id */
        break;
      case 53:
        YYTRACE("cmd ::= DROP TABLE id");
// #line 139 "D:/workspace/java/sqlite/work/parse.y"
{sqliteDropTable(pParse,yypParser.offset(0).minor.yy180);}
// #line 3817 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for DROP */
        /* No destructor defined for TABLE */
        break;
      case 54:
        YYTRACE("cmd ::= select");
// #line 143 "D:/workspace/java/sqlite/work/parse.y"
{
  sqliteSelect(pParse, yypParser.offset(0).minor.yy39, SRT_Callback, 0);
  sqliteSelectDelete(yypParser.offset(0).minor.yy39);
}
// #line 3828 "D:/workspace/java/sqlite/work/parse.java"
        break;
      case 55:
        YYTRACE("select ::= oneselect");
// #line 153 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy39 = yypParser.offset(0).minor.yy39;}
// #line 3834 "D:/workspace/java/sqlite/work/parse.java"
        break;
      case 56:
        YYTRACE("select ::= select joinop oneselect");
// #line 154 "D:/workspace/java/sqlite/work/parse.y"
{
    yypParser.offset(0).minor.yy39.op = yypParser.offset(-1).minor.yy64;
    yypParser.offset(0).minor.yy39.pPrior = yypParser.offset(-2).minor.yy39;
    yygotominor.yy39 = yypParser.offset(0).minor.yy39;
}
// #line 3844 "D:/workspace/java/sqlite/work/parse.java"
        break;
      case 57:
        YYTRACE("joinop ::= UNION");
// #line 160 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy64 = TK_UNION;}
// #line 3850 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for UNION */
        break;
      case 58:
        YYTRACE("joinop ::= UNION ALL");
// #line 161 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy64 = TK_ALL;}
// #line 3857 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for UNION */
        /* No destructor defined for ALL */
        break;
      case 59:
        YYTRACE("joinop ::= INTERSECT");
// #line 162 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy64 = TK_INTERSECT;}
// #line 3865 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for INTERSECT */
        break;
      case 60:
        YYTRACE("joinop ::= EXCEPT");
// #line 163 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy64 = TK_EXCEPT;}
// #line 3872 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for EXCEPT */
        break;
      case 61:
        YYTRACE("oneselect ::= SELECT distinct selcollist from where_opt groupby_opt having_opt orderby_opt");
// #line 165 "D:/workspace/java/sqlite/work/parse.y"
{
  yygotominor.yy39 = sqliteSelectNew(yypParser.offset(-5).minor.yy154,yypParser.offset(-4).minor.yy204,yypParser.offset(-3).minor.yy234,yypParser.offset(-2).minor.yy154,yypParser.offset(-1).minor.yy234,yypParser.offset(0).minor.yy154,yypParser.offset(-6).minor.yy64);
}
// #line 3881 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for SELECT */
        break;
      case 62:
        YYTRACE("distinct ::= DISTINCT");
// #line 173 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy64 = 1;}
// #line 3888 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for DISTINCT */
        break;
      case 63:
        YYTRACE("distinct ::= ALL");
// #line 174 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy64 = 0;}
// #line 3895 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for ALL */
        break;
      case 64:
        YYTRACE("distinct ::=");
// #line 175 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy64 = 0;}
// #line 3902 "D:/workspace/java/sqlite/work/parse.java"
        break;
      case 65:
        YYTRACE("sclp ::= selcollist COMMA");
// #line 185 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy154 = yypParser.offset(-1).minor.yy154;}
// #line 3908 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for COMMA */
        break;
      case 66:
        YYTRACE("sclp ::=");
// #line 186 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy154 = null;}
// #line 3915 "D:/workspace/java/sqlite/work/parse.java"
        break;
      case 67:
        YYTRACE("selcollist ::= STAR");
// #line 187 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy154 = null;}
// #line 3921 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for STAR */
        break;
      case 68:
        YYTRACE("selcollist ::= sclp expr");
// #line 188 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy154 = sqliteExprListAppend(yypParser.offset(-1).minor.yy154,yypParser.offset(0).minor.yy234,0);}
// #line 3928 "D:/workspace/java/sqlite/work/parse.java"
        break;
      case 69:
        YYTRACE("selcollist ::= sclp expr as id");
// #line 189 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy154 = sqliteExprListAppend(yypParser.offset(-3).minor.yy154,yypParser.offset(-2).minor.yy234,yypParser.offset(0).minor.yy180);}
// #line 3934 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for as */
        break;
      case 70:
        YYTRACE("as ::=");
        break;
      case 71:
        YYTRACE("as ::= AS");
        /* No destructor defined for AS */
        break;
      case 72:
        YYTRACE("from ::= FROM seltablist");
// #line 201 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy204 = yypParser.offset(0).minor.yy204;}
// #line 3948 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for FROM */
        break;
      case 73:
        YYTRACE("stl_prefix ::= seltablist COMMA");
// #line 202 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy204 = yypParser.offset(-1).minor.yy204;}
// #line 3955 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for COMMA */
        break;
      case 74:
        YYTRACE("stl_prefix ::=");
// #line 203 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy204 = null;}
// #line 3962 "D:/workspace/java/sqlite/work/parse.java"
        break;
      case 75:
        YYTRACE("seltablist ::= stl_prefix id");
// #line 204 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy204 = sqliteIdListAppend(yypParser.offset(-1).minor.yy204,yypParser.offset(0).minor.yy180);}
// #line 3968 "D:/workspace/java/sqlite/work/parse.java"
        break;
      case 76:
        YYTRACE("seltablist ::= stl_prefix id as id");
// #line 206 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy204 = sqliteIdListAppend(yypParser.offset(-3).minor.yy204,yypParser.offset(-2).minor.yy180);
    sqliteIdListAddAlias(yygotominor.yy204,yypParser.offset(0).minor.yy180);}
// #line 3975 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for as */
        break;
      case 77:
        YYTRACE("orderby_opt ::=");
// #line 216 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy154 = null;}
// #line 3982 "D:/workspace/java/sqlite/work/parse.java"
        break;
      case 78:
        YYTRACE("orderby_opt ::= ORDER BY sortlist");
// #line 217 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy154 = yypParser.offset(0).minor.yy154;}
// #line 3988 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for ORDER */
        /* No destructor defined for BY */
        break;
      case 79:
        YYTRACE("sortlist ::= sortlist COMMA sortitem sortorder");
// #line 218 "D:/workspace/java/sqlite/work/parse.y"
{
  yygotominor.yy154 = sqliteExprListAppend(yypParser.offset(-3).minor.yy154,yypParser.offset(-1).minor.yy234,0);
  yygotominor.yy154.a[yygotominor.yy154.nExpr-1].sortOrder = yypParser.offset(0).minor.yy64;   /* 0 for ascending order, 1 for decending */
}
// #line 3999 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for COMMA */
        break;
      case 80:
        YYTRACE("sortlist ::= sortitem sortorder");
// #line 222 "D:/workspace/java/sqlite/work/parse.y"
{
  yygotominor.yy154 = sqliteExprListAppend(0,yypParser.offset(-1).minor.yy234,0);
  yygotominor.yy154.a[0].sortOrder = yypParser.offset(0).minor.yy64;
}
// #line 4009 "D:/workspace/java/sqlite/work/parse.java"
        break;
      case 81:
        YYTRACE("sortitem ::= expr");
// #line 226 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy234 = yypParser.offset(0).minor.yy234;}
// #line 4015 "D:/workspace/java/sqlite/work/parse.java"
        break;
      case 82:
        YYTRACE("sortorder ::= ASC");
// #line 230 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy64 = 0;}
// #line 4021 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for ASC */
        break;
      case 83:
        YYTRACE("sortorder ::= DESC");
// #line 231 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy64 = 1;}
// #line 4028 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for DESC */
        break;
      case 84:
        YYTRACE("sortorder ::=");
// #line 232 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy64 = 0;}
// #line 4035 "D:/workspace/java/sqlite/work/parse.java"
        break;
      case 85:
        YYTRACE("groupby_opt ::=");
// #line 236 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy154 = null;}
// #line 4041 "D:/workspace/java/sqlite/work/parse.java"
        break;
      case 86:
        YYTRACE("groupby_opt ::= GROUP BY exprlist");
// #line 237 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy154 = yypParser.offset(0).minor.yy154;}
// #line 4047 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for GROUP */
        /* No destructor defined for BY */
        break;
      case 87:
        YYTRACE("having_opt ::=");
// #line 241 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy234 = null;}
// #line 4055 "D:/workspace/java/sqlite/work/parse.java"
        break;
      case 88:
        YYTRACE("having_opt ::= HAVING expr");
// #line 242 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy234 = yypParser.offset(0).minor.yy234;}
// #line 4061 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for HAVING */
        break;
      case 89:
        YYTRACE("cmd ::= DELETE FROM id where_opt");
// #line 246 "D:/workspace/java/sqlite/work/parse.y"
{sqliteDeleteFrom(pParse, yypParser.offset(-1).minor.yy180, yypParser.offset(0).minor.yy234);}
// #line 4068 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for DELETE */
        /* No destructor defined for FROM */
        break;
      case 90:
        YYTRACE("where_opt ::=");
// #line 251 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy234 = null;}
// #line 4076 "D:/workspace/java/sqlite/work/parse.java"
        break;
      case 91:
        YYTRACE("where_opt ::= WHERE expr");
// #line 252 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy234 = yypParser.offset(0).minor.yy234;}
// #line 4082 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for WHERE */
        break;
      case 92:
        YYTRACE("cmd ::= UPDATE id SET setlist where_opt");
// #line 258 "D:/workspace/java/sqlite/work/parse.y"
{sqliteUpdate(pParse,yypParser.offset(-3).minor.yy180,yypParser.offset(-1).minor.yy154,yypParser.offset(0).minor.yy234);}
// #line 4089 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for UPDATE */
        /* No destructor defined for SET */
        break;
      case 93:
        YYTRACE("setlist ::= setlist COMMA id EQ expr");
// #line 261 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy154 = sqliteExprListAppend(yypParser.offset(-4).minor.yy154,yypParser.offset(0).minor.yy234,yypParser.offset(-2).minor.yy180);}
// #line 4097 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for COMMA */
        /* No destructor defined for EQ */
        break;
      case 94:
        YYTRACE("setlist ::= id EQ expr");
// #line 262 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy154 = sqliteExprListAppend(0,yypParser.offset(0).minor.yy234,yypParser.offset(-2).minor.yy180);}
// #line 4105 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for EQ */
        break;
      case 95:
        YYTRACE("cmd ::= INSERT INTO id inscollist_opt VALUES LP itemlist RP");
// #line 265 "D:/workspace/java/sqlite/work/parse.y"
{sqliteInsert(pParse, yypParser.offset(-5).minor.yy180, yypParser.offset(-1).minor.yy154, 0, yypParser.offset(-4).minor.yy204);}
// #line 4112 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for INSERT */
        /* No destructor defined for INTO */
        /* No destructor defined for VALUES */
        /* No destructor defined for LP */
        /* No destructor defined for RP */
        break;
      case 96:
        YYTRACE("cmd ::= INSERT INTO id inscollist_opt select");
// #line 267 "D:/workspace/java/sqlite/work/parse.y"
{sqliteInsert(pParse, yypParser.offset(-2).minor.yy180, 0, yypParser.offset(0).minor.yy39, yypParser.offset(-1).minor.yy204);}
// #line 4123 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for INSERT */
        /* No destructor defined for INTO */
        break;
      case 97:
        YYTRACE("itemlist ::= itemlist COMMA item");
// #line 275 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy154 = sqliteExprListAppend(yypParser.offset(-2).minor.yy154,yypParser.offset(0).minor.yy234,0);}
// #line 4131 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for COMMA */
        break;
      case 98:
        YYTRACE("itemlist ::= item");
// #line 276 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy154 = sqliteExprListAppend(0,yypParser.offset(0).minor.yy234,0);}
// #line 4138 "D:/workspace/java/sqlite/work/parse.java"
        break;
      case 99:
        YYTRACE("item ::= INTEGER");
// #line 277 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy234 = sqliteExpr(TK_INTEGER, 0, 0, yypParser.offset(0).minor.yy0);}
// #line 4144 "D:/workspace/java/sqlite/work/parse.java"
        break;
      case 100:
        YYTRACE("item ::= PLUS INTEGER");
// #line 278 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy234 = sqliteExpr(TK_INTEGER, 0, 0, yypParser.offset(0).minor.yy0);}
// #line 4150 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for PLUS */
        break;
      case 101:
        YYTRACE("item ::= MINUS INTEGER");
// #line 279 "D:/workspace/java/sqlite/work/parse.y"
{
  yygotominor.yy234 = sqliteExpr(TK_UMINUS, 0, 0, 0);
  yygotominor.yy234.pLeft = sqliteExpr(TK_INTEGER, 0, 0, yypParser.offset(0).minor.yy0);
}
// #line 4160 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for MINUS */
        break;
      case 102:
        YYTRACE("item ::= FLOAT");
// #line 283 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy234 = sqliteExpr(TK_FLOAT, 0, 0, yypParser.offset(0).minor.yy0);}
// #line 4167 "D:/workspace/java/sqlite/work/parse.java"
        break;
      case 103:
        YYTRACE("item ::= PLUS FLOAT");
// #line 284 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy234 = sqliteExpr(TK_FLOAT, 0, 0, yypParser.offset(0).minor.yy0);}
// #line 4173 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for PLUS */
        break;
      case 104:
        YYTRACE("item ::= MINUS FLOAT");
// #line 285 "D:/workspace/java/sqlite/work/parse.y"
{
  yygotominor.yy234 = sqliteExpr(TK_UMINUS, 0, 0, 0);
  yygotominor.yy234.pLeft = sqliteExpr(TK_FLOAT, 0, 0, yypParser.offset(0).minor.yy0);
}
// #line 4183 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for MINUS */
        break;
      case 105:
        YYTRACE("item ::= STRING");
// #line 289 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy234 = sqliteExpr(TK_STRING, 0, 0, yypParser.offset(0).minor.yy0);}
// #line 4190 "D:/workspace/java/sqlite/work/parse.java"
        break;
      case 106:
        YYTRACE("item ::= NULL");
// #line 290 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy234 = sqliteExpr(TK_NULL, 0, 0, 0);}
// #line 4196 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for NULL */
        break;
      case 107:
        YYTRACE("inscollist_opt ::=");
// #line 297 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy204 = null;}
// #line 4203 "D:/workspace/java/sqlite/work/parse.java"
        break;
      case 108:
        YYTRACE("inscollist_opt ::= LP inscollist RP");
// #line 298 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy204 = yypParser.offset(-1).minor.yy204;}
// #line 4209 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for LP */
        /* No destructor defined for RP */
        break;
      case 109:
        YYTRACE("inscollist ::= inscollist COMMA id");
// #line 299 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy204 = sqliteIdListAppend(yypParser.offset(-2).minor.yy204,yypParser.offset(0).minor.yy180);}
// #line 4217 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for COMMA */
        break;
      case 110:
        YYTRACE("inscollist ::= id");
// #line 300 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy204 = sqliteIdListAppend(0,yypParser.offset(0).minor.yy180);}
// #line 4224 "D:/workspace/java/sqlite/work/parse.java"
        break;
      case 111:
        YYTRACE("expr ::= LP expr RP");
// #line 315 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy234 = yypParser.offset(-1).minor.yy234; sqliteExprSpan(yygotominor.yy234,yypParser.offset(-2).minor.yy0,yypParser.offset(0).minor.yy0);}
// #line 4230 "D:/workspace/java/sqlite/work/parse.java"
        break;
      case 112:
        YYTRACE("expr ::= ID");
// #line 316 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy234 = sqliteExpr(TK_ID, 0, 0, yypParser.offset(0).minor.yy0);}
// #line 4236 "D:/workspace/java/sqlite/work/parse.java"
        break;
      case 113:
        YYTRACE("expr ::= NULL");
// #line 317 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy234 = sqliteExpr(TK_NULL, 0, 0, yypParser.offset(0).minor.yy0);}
// #line 4242 "D:/workspace/java/sqlite/work/parse.java"
        break;
      case 114:
        YYTRACE("expr ::= id DOT id");
// #line 318 "D:/workspace/java/sqlite/work/parse.y"
{
  Expr temp1 = sqliteExpr(TK_ID, 0, 0, yypParser.offset(-2).minor.yy180);
  Expr temp2 = sqliteExpr(TK_ID, 0, 0, yypParser.offset(0).minor.yy180);
  yygotominor.yy234 = sqliteExpr(TK_DOT, temp1, temp2, 0);
}
// #line 4252 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for DOT */
        break;
      case 115:
        YYTRACE("expr ::= INTEGER");
// #line 323 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy234 = sqliteExpr(TK_INTEGER, 0, 0, yypParser.offset(0).minor.yy0);}
// #line 4259 "D:/workspace/java/sqlite/work/parse.java"
        break;
      case 116:
        YYTRACE("expr ::= FLOAT");
// #line 324 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy234 = sqliteExpr(TK_FLOAT, 0, 0, yypParser.offset(0).minor.yy0);}
// #line 4265 "D:/workspace/java/sqlite/work/parse.java"
        break;
      case 117:
        YYTRACE("expr ::= STRING");
// #line 325 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy234 = sqliteExpr(TK_STRING, 0, 0, yypParser.offset(0).minor.yy0);}
// #line 4271 "D:/workspace/java/sqlite/work/parse.java"
        break;
      case 118:
        YYTRACE("expr ::= ID LP exprlist RP");
// #line 326 "D:/workspace/java/sqlite/work/parse.y"
{
  yygotominor.yy234 = sqliteExprFunction(yypParser.offset(-1).minor.yy154, yypParser.offset(-3).minor.yy0);
  sqliteExprSpan(yygotominor.yy234,yypParser.offset(-3).minor.yy0,yypParser.offset(0).minor.yy0);
}
// #line 4280 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for LP */
        break;
      case 119:
        YYTRACE("expr ::= ID LP STAR RP");
// #line 330 "D:/workspace/java/sqlite/work/parse.y"
{
  yygotominor.yy234 = sqliteExprFunction(0, yypParser.offset(-3).minor.yy0);
  sqliteExprSpan(yygotominor.yy234,yypParser.offset(-3).minor.yy0,yypParser.offset(0).minor.yy0);
}
// #line 4290 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for LP */
        /* No destructor defined for STAR */
        break;
      case 120:
        YYTRACE("expr ::= expr AND expr");
// #line 334 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy234 = sqliteExpr(TK_AND, yypParser.offset(-2).minor.yy234, yypParser.offset(0).minor.yy234, 0);}
// #line 4298 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for AND */
        break;
      case 121:
        YYTRACE("expr ::= expr OR expr");
// #line 335 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy234 = sqliteExpr(TK_OR, yypParser.offset(-2).minor.yy234, yypParser.offset(0).minor.yy234, 0);}
// #line 4305 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for OR */
        break;
      case 122:
        YYTRACE("expr ::= expr LT expr");
// #line 336 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy234 = sqliteExpr(TK_LT, yypParser.offset(-2).minor.yy234, yypParser.offset(0).minor.yy234, 0);}
// #line 4312 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for LT */
        break;
      case 123:
        YYTRACE("expr ::= expr GT expr");
// #line 337 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy234 = sqliteExpr(TK_GT, yypParser.offset(-2).minor.yy234, yypParser.offset(0).minor.yy234, 0);}
// #line 4319 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for GT */
        break;
      case 124:
        YYTRACE("expr ::= expr LE expr");
// #line 338 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy234 = sqliteExpr(TK_LE, yypParser.offset(-2).minor.yy234, yypParser.offset(0).minor.yy234, 0);}
// #line 4326 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for LE */
        break;
      case 125:
        YYTRACE("expr ::= expr GE expr");
// #line 339 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy234 = sqliteExpr(TK_GE, yypParser.offset(-2).minor.yy234, yypParser.offset(0).minor.yy234, 0);}
// #line 4333 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for GE */
        break;
      case 126:
        YYTRACE("expr ::= expr NE expr");
// #line 340 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy234 = sqliteExpr(TK_NE, yypParser.offset(-2).minor.yy234, yypParser.offset(0).minor.yy234, 0);}
// #line 4340 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for NE */
        break;
      case 127:
        YYTRACE("expr ::= expr EQ expr");
// #line 341 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy234 = sqliteExpr(TK_EQ, yypParser.offset(-2).minor.yy234, yypParser.offset(0).minor.yy234, 0);}
// #line 4347 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for EQ */
        break;
      case 128:
        YYTRACE("expr ::= expr LIKE expr");
// #line 342 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy234 = sqliteExpr(TK_LIKE, yypParser.offset(-2).minor.yy234, yypParser.offset(0).minor.yy234, 0);}
// #line 4354 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for LIKE */
        break;
      case 129:
        YYTRACE("expr ::= expr NOT LIKE expr");
// #line 343 "D:/workspace/java/sqlite/work/parse.y"
{
  yygotominor.yy234 = sqliteExpr(TK_LIKE, yypParser.offset(-3).minor.yy234, yypParser.offset(0).minor.yy234, 0);
  yygotominor.yy234 = sqliteExpr(TK_NOT, yygotominor.yy234, 0, 0);
  sqliteExprSpan(yygotominor.yy234,yypParser.offset(-3).minor.yy234.span,yypParser.offset(0).minor.yy234.span);
}
// #line 4365 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for NOT */
        /* No destructor defined for LIKE */
        break;
      case 130:
        YYTRACE("expr ::= expr GLOB expr");
// #line 348 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy234 = sqliteExpr(TK_GLOB,yypParser.offset(-2).minor.yy234,yypParser.offset(0).minor.yy234,0);}
// #line 4373 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for GLOB */
        break;
      case 131:
        YYTRACE("expr ::= expr NOT GLOB expr");
// #line 349 "D:/workspace/java/sqlite/work/parse.y"
{
  yygotominor.yy234 = sqliteExpr(TK_GLOB, yypParser.offset(-3).minor.yy234, yypParser.offset(0).minor.yy234, 0);
  yygotominor.yy234 = sqliteExpr(TK_NOT, yygotominor.yy234, 0, 0);
  sqliteExprSpan(yygotominor.yy234,yypParser.offset(-3).minor.yy234.span,yypParser.offset(0).minor.yy234.span);
}
// #line 4384 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for NOT */
        /* No destructor defined for GLOB */
        break;
      case 132:
        YYTRACE("expr ::= expr PLUS expr");
// #line 354 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy234 = sqliteExpr(TK_PLUS, yypParser.offset(-2).minor.yy234, yypParser.offset(0).minor.yy234, 0);}
// #line 4392 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for PLUS */
        break;
      case 133:
        YYTRACE("expr ::= expr MINUS expr");
// #line 355 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy234 = sqliteExpr(TK_MINUS, yypParser.offset(-2).minor.yy234, yypParser.offset(0).minor.yy234, 0);}
// #line 4399 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for MINUS */
        break;
      case 134:
        YYTRACE("expr ::= expr STAR expr");
// #line 356 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy234 = sqliteExpr(TK_STAR, yypParser.offset(-2).minor.yy234, yypParser.offset(0).minor.yy234, 0);}
// #line 4406 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for STAR */
        break;
      case 135:
        YYTRACE("expr ::= expr SLASH expr");
// #line 357 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy234 = sqliteExpr(TK_SLASH, yypParser.offset(-2).minor.yy234, yypParser.offset(0).minor.yy234, 0);}
// #line 4413 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for SLASH */
        break;
      case 136:
        YYTRACE("expr ::= expr CONCAT expr");
// #line 358 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy234 = sqliteExpr(TK_CONCAT, yypParser.offset(-2).minor.yy234, yypParser.offset(0).minor.yy234, 0);}
// #line 4420 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for CONCAT */
        break;
      case 137:
        YYTRACE("expr ::= expr ISNULL");
// #line 359 "D:/workspace/java/sqlite/work/parse.y"
{
  yygotominor.yy234 = sqliteExpr(TK_ISNULL, yypParser.offset(-1).minor.yy234, 0, 0);
  sqliteExprSpan(yygotominor.yy234,yypParser.offset(-1).minor.yy234.span,yypParser.offset(0).minor.yy0);
}
// #line 4430 "D:/workspace/java/sqlite/work/parse.java"
        break;
      case 138:
        YYTRACE("expr ::= expr NOTNULL");
// #line 363 "D:/workspace/java/sqlite/work/parse.y"
{
  yygotominor.yy234 = sqliteExpr(TK_NOTNULL, yypParser.offset(-1).minor.yy234, 0, 0);
  sqliteExprSpan(yygotominor.yy234,yypParser.offset(-1).minor.yy234.span,yypParser.offset(0).minor.yy0);
}
// #line 4439 "D:/workspace/java/sqlite/work/parse.java"
        break;
      case 139:
        YYTRACE("expr ::= NOT expr");
// #line 367 "D:/workspace/java/sqlite/work/parse.y"
{
  yygotominor.yy234 = sqliteExpr(TK_NOT, yypParser.offset(0).minor.yy234, 0, 0);
  sqliteExprSpan(yygotominor.yy234,yypParser.offset(-1).minor.yy0,yypParser.offset(0).minor.yy234.span);
}
// #line 4448 "D:/workspace/java/sqlite/work/parse.java"
        break;
      case 140:
        YYTRACE("expr ::= MINUS expr");
// #line 371 "D:/workspace/java/sqlite/work/parse.y"
{
  yygotominor.yy234 = sqliteExpr(TK_UMINUS, yypParser.offset(0).minor.yy234, 0, 0);
  sqliteExprSpan(yygotominor.yy234,yypParser.offset(-1).minor.yy0,yypParser.offset(0).minor.yy234.span);
}
// #line 4457 "D:/workspace/java/sqlite/work/parse.java"
        break;
      case 141:
        YYTRACE("expr ::= PLUS expr");
// #line 375 "D:/workspace/java/sqlite/work/parse.y"
{
  yygotominor.yy234 = yypParser.offset(0).minor.yy234;
  sqliteExprSpan(yygotominor.yy234,yypParser.offset(-1).minor.yy0,yypParser.offset(0).minor.yy234.span);
}
// #line 4466 "D:/workspace/java/sqlite/work/parse.java"
        break;
      case 142:
        YYTRACE("expr ::= LP select RP");
// #line 379 "D:/workspace/java/sqlite/work/parse.y"
{
  yygotominor.yy234 = sqliteExpr(TK_SELECT, 0, 0, 0);
  yygotominor.yy234.pSelect = yypParser.offset(-1).minor.yy39;
  sqliteExprSpan(yygotominor.yy234,yypParser.offset(-2).minor.yy0,yypParser.offset(0).minor.yy0);
}
// #line 4476 "D:/workspace/java/sqlite/work/parse.java"
        break;
      case 143:
        YYTRACE("expr ::= expr BETWEEN expr AND expr");
// #line 384 "D:/workspace/java/sqlite/work/parse.y"
{
  ExprList pList = sqliteExprListAppend(0, yypParser.offset(-2).minor.yy234, 0);
  pList = sqliteExprListAppend(pList, yypParser.offset(0).minor.yy234, 0);
  yygotominor.yy234 = sqliteExpr(TK_BETWEEN, yypParser.offset(-4).minor.yy234, 0, 0);
  yygotominor.yy234.pList = pList;
  sqliteExprSpan(yygotominor.yy234,yypParser.offset(-4).minor.yy234.span,yypParser.offset(0).minor.yy234.span);
}
// #line 4488 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for BETWEEN */
        /* No destructor defined for AND */
        break;
      case 144:
        YYTRACE("expr ::= expr NOT BETWEEN expr AND expr");
// #line 391 "D:/workspace/java/sqlite/work/parse.y"
{
  ExprList pList = sqliteExprListAppend(0, yypParser.offset(-2).minor.yy234, 0);
  pList = sqliteExprListAppend(pList, yypParser.offset(0).minor.yy234, 0);
  yygotominor.yy234 = sqliteExpr(TK_BETWEEN, yypParser.offset(-5).minor.yy234, 0, 0);
  yygotominor.yy234.pList = pList;
  yygotominor.yy234 = sqliteExpr(TK_NOT, yygotominor.yy234, 0, 0);
  sqliteExprSpan(yygotominor.yy234,yypParser.offset(-5).minor.yy234.span,yypParser.offset(0).minor.yy234.span);
}
// #line 4503 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for NOT */
        /* No destructor defined for BETWEEN */
        /* No destructor defined for AND */
        break;
      case 145:
        YYTRACE("expr ::= expr IN LP exprlist RP");
// #line 399 "D:/workspace/java/sqlite/work/parse.y"
{
  yygotominor.yy234 = sqliteExpr(TK_IN, yypParser.offset(-4).minor.yy234, 0, 0);
  yygotominor.yy234.pList = yypParser.offset(-1).minor.yy154;
  sqliteExprSpan(yygotominor.yy234,yypParser.offset(-4).minor.yy234.span,yypParser.offset(0).minor.yy0);
}
// #line 4516 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for IN */
        /* No destructor defined for LP */
        break;
      case 146:
        YYTRACE("expr ::= expr IN LP select RP");
// #line 404 "D:/workspace/java/sqlite/work/parse.y"
{
  yygotominor.yy234 = sqliteExpr(TK_IN, yypParser.offset(-4).minor.yy234, 0, 0);
  yygotominor.yy234.pSelect = yypParser.offset(-1).minor.yy39;
  sqliteExprSpan(yygotominor.yy234,yypParser.offset(-4).minor.yy234.span,yypParser.offset(0).minor.yy0);
}
// #line 4528 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for IN */
        /* No destructor defined for LP */
        break;
      case 147:
        YYTRACE("expr ::= expr NOT IN LP exprlist RP");
// #line 409 "D:/workspace/java/sqlite/work/parse.y"
{
  yygotominor.yy234 = sqliteExpr(TK_IN, yypParser.offset(-5).minor.yy234, 0, 0);
  yygotominor.yy234.pList = yypParser.offset(-1).minor.yy154;
  yygotominor.yy234 = sqliteExpr(TK_NOT, yygotominor.yy234, 0, 0);
  sqliteExprSpan(yygotominor.yy234,yypParser.offset(-5).minor.yy234.span,yypParser.offset(0).minor.yy0);
}
// #line 4541 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for NOT */
        /* No destructor defined for IN */
        /* No destructor defined for LP */
        break;
      case 148:
        YYTRACE("expr ::= expr NOT IN LP select RP");
// #line 415 "D:/workspace/java/sqlite/work/parse.y"
{
  yygotominor.yy234 = sqliteExpr(TK_IN, yypParser.offset(-5).minor.yy234, 0, 0);
  yygotominor.yy234.pSelect = yypParser.offset(-1).minor.yy39;
  yygotominor.yy234 = sqliteExpr(TK_NOT, yygotominor.yy234, 0, 0);
  sqliteExprSpan(yygotominor.yy234,yypParser.offset(-5).minor.yy234.span,yypParser.offset(0).minor.yy0);
}
// #line 4555 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for NOT */
        /* No destructor defined for IN */
        /* No destructor defined for LP */
        break;
      case 149:
        YYTRACE("exprlist ::= exprlist COMMA expritem");
// #line 430 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy154 = sqliteExprListAppend(yypParser.offset(-2).minor.yy154,yypParser.offset(0).minor.yy234,0);}
// #line 4564 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for COMMA */
        break;
      case 150:
        YYTRACE("exprlist ::= expritem");
// #line 431 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy154 = sqliteExprListAppend(0,yypParser.offset(0).minor.yy234,0);}
// #line 4571 "D:/workspace/java/sqlite/work/parse.java"
        break;
      case 151:
        YYTRACE("expritem ::= expr");
// #line 432 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy234 = yypParser.offset(0).minor.yy234;}
// #line 4577 "D:/workspace/java/sqlite/work/parse.java"
        break;
      case 152:
        YYTRACE("expritem ::=");
// #line 433 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy234 = null;}
// #line 4583 "D:/workspace/java/sqlite/work/parse.java"
        break;
      case 153:
        YYTRACE("cmd ::= CREATE uniqueflag INDEX id ON id LP idxlist RP");
// #line 437 "D:/workspace/java/sqlite/work/parse.y"
{sqliteCreateIndex(pParse, yypParser.offset(-5).minor.yy180, yypParser.offset(-3).minor.yy180, yypParser.offset(-1).minor.yy204, yypParser.offset(-8).minor.yy0, yypParser.offset(0).minor.yy0);}
// #line 4589 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for uniqueflag */
        /* No destructor defined for INDEX */
        /* No destructor defined for ON */
        /* No destructor defined for LP */
        break;
      case 154:
        YYTRACE("uniqueflag ::= UNIQUE");
        /* No destructor defined for UNIQUE */
        break;
      case 155:
        YYTRACE("uniqueflag ::=");
        break;
      case 156:
        YYTRACE("idxlist ::= idxlist COMMA idxitem");
// #line 446 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy204 = sqliteIdListAppend(yypParser.offset(-2).minor.yy204,yypParser.offset(0).minor.yy180);}
// #line 4606 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for COMMA */
        break;
      case 157:
        YYTRACE("idxlist ::= idxitem");
// #line 448 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy204 = sqliteIdListAppend(0,yypParser.offset(0).minor.yy180);}
// #line 4613 "D:/workspace/java/sqlite/work/parse.java"
        break;
      case 158:
        YYTRACE("idxitem ::= id");
// #line 449 "D:/workspace/java/sqlite/work/parse.y"
{yygotominor.yy180 = yypParser.offset(0).minor.yy180;}
// #line 4619 "D:/workspace/java/sqlite/work/parse.java"
        break;
      case 159:
        YYTRACE("cmd ::= DROP INDEX id");
// #line 451 "D:/workspace/java/sqlite/work/parse.y"
{sqliteDropIndex(pParse, yypParser.offset(0).minor.yy180);}
// #line 4625 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for DROP */
        /* No destructor defined for INDEX */
        break;
      case 160:
        YYTRACE("cmd ::= COPY id FROM id USING DELIMITERS STRING");
// #line 454 "D:/workspace/java/sqlite/work/parse.y"
{sqliteCopy(pParse,yypParser.offset(-5).minor.yy180,yypParser.offset(-3).minor.yy180,yypParser.offset(0).minor.yy0);}
// #line 4633 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for COPY */
        /* No destructor defined for FROM */
        /* No destructor defined for USING */
        /* No destructor defined for DELIMITERS */
        break;
      case 161:
        YYTRACE("cmd ::= COPY id FROM id");
// #line 456 "D:/workspace/java/sqlite/work/parse.y"
{sqliteCopy(pParse,yypParser.offset(-2).minor.yy180,yypParser.offset(0).minor.yy180,0);}
// #line 4643 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for COPY */
        /* No destructor defined for FROM */
        break;
      case 162:
        YYTRACE("cmd ::= VACUUM");
// #line 458 "D:/workspace/java/sqlite/work/parse.y"
{sqliteVacuum(pParse,0);}
// #line 4651 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for VACUUM */
        break;
      case 163:
        YYTRACE("cmd ::= VACUUM id");
// #line 459 "D:/workspace/java/sqlite/work/parse.y"
{sqliteVacuum(pParse,yypParser.offset(0).minor.yy180);}
// #line 4658 "D:/workspace/java/sqlite/work/parse.java"
        /* No destructor defined for VACUUM */
        break;
  };
  yygoto = yyRuleInfo[yyruleno].lhs;
  yysize = yyRuleInfo[yyruleno].nrhs;
  yypParser.idx -= yysize;
  yypParser.top -= yysize;
  yyact = yy_find_parser_action(yypParser,yygoto);
  if( yyact < 305 ){
    yy_shift(yypParser,yyact,yygoto,yygotominor);
  }else if( yyact == 305 + 164 + 1 ){
    yy_accept(yypParser ,pParse);
  }
}

/*
** The following code executes when the parse fails
*/
static void yy_parse_failed(
  yyParser yypParser           /* The parser */
  ,Parse pParse              /* Extra arguments (if any) */
){
//#ifndef NDEBUG
  if( yyTraceFILE != null){
    trace(String.format("%sFail!\n",yyTracePrompt).getBytes());
  }
//#endif
  while( yypParser.idx>=0 ) yy_pop_parser_stack(yypParser);
  /* Here code is inserted which will be executed whenever the
  ** parser fails */
}

/*
** The following code executes when a syntax error first occurs.
*/
static void yy_syntax_error(
  yyParser yypParser,           /* The parser */
  int yymajor,                   /* The major type of the error token */
  YYMINORTYPE yyminor            /* The minor type of the error token */
  ,Parse pParse               /* Extra arguments (if any) */
){
//#define (yyminor.yy0) (yyminor.yy0)
// #line 34 "D:/workspace/java/sqlite/work/parse.y"

  sqliteSetString(pParse.zErrMsg,"syntax error",0);
  pParse.sErrToken = (yyminor.yy0);

// #line 4705 "D:/workspace/java/sqlite/work/parse.java"
}

/*
** The following is executed when the parser accepts
*/
public static void yy_accept(
  yyParser yypParser           /* The parser */
  ,Parse pParse              /* Extra arguments (if any) */
){
//#ifndef NDEBUG
  if( yyTraceFILE != null){
    trace(String.format("%sAccept!\n",yyTracePrompt).getBytes());
  }
//#endif
  while( yypParser.idx>=0 ) yy_pop_parser_stack(yypParser);
  /* Here code is inserted which will be executed whenever the
  ** parser accepts */
}

/* The main parser program.
** The first argument is a pointer to a structure obtained from
** "ParseAlloc" which describes the current state of the parser.
** The second argument is the major token number.  The third is
** the minor token.  The fourth optional argument is whatever the
** user wants (and specified in the grammar) and is available for
** use by the action routines.
**
** Inputs:
** <ul>
** <li> A pointer to the parser (an opaque structure.)
** <li> The major token number.
** <li> The minor token number.
** <li> An option argument of a grammar-specified type.
** </ul>
**
** Outputs:
** None.
*/
/* SQLITE MODIFICATION: Give the function file scope */
public static void Parse(
  Object yyp,                   /* The parser */
  int yymajor,                 /* The major token code number */
  Token yyminor       /* The value for the token */
  ,Parse pParse
){
  YYMINORTYPE yyminorunion = new YYMINORTYPE();
  int yyact;            /* The parser action. */
  boolean yyendofinput;     /* True if we are at the end of input */
  int yyerrorhit = 0;   /* True if yymajor has invoked an error */
  yyParser yypParser;  /* The parser */

  /* (re)initialize the parser, if necessary */
  yypParser = (yyParser)yyp;
  if( yypParser.idx<0 ){
    if( yymajor==0 ) return;
    yypParser.idx = 0;
    yypParser.errcnt = -1;
//    yypParser.top = &yypParser.stack[0];
    yypParser.top = 0;
    yypParser.top().stateno = 0;
    yypParser.top().major = 0;
  }
  yyminorunion.yy0 = yyminor;
  yyendofinput = (yymajor==0);

//#ifndef NDEBUG
  if( yyTraceFILE != null ){
    trace(String.format("%sInput %s\n",yyTracePrompt,yyTokenName[yymajor]).getBytes());
  }
//#endif

  do{
    yyact = yy_find_parser_action(yypParser,yymajor);
    if( yyact<305 ){
      yy_shift(yypParser,yyact,yymajor,yyminorunion);
      yypParser.errcnt--;
      if( yyendofinput && yypParser.idx>=0 ){
        yymajor = 0;
      }else{
        yymajor = 130;
      }
    }else if( yyact < 305 + 164 ){
      yy_reduce(yypParser,yyact-305 ,pParse);
    }else if( yyact == YY_ERROR_ACTION ){
//#ifndef NDEBUG
      if( yyTraceFILE != null){
        trace(String.format("%sSyntax Error!\n",yyTracePrompt).getBytes());
      }
//#endif
//#ifdef 93
      /* A syntax error has occurred.
      ** The response to an error depends upon whether or not the
      ** grammar defines an error token "ERROR".
      **
      ** This is what we do if the grammar does define ERROR:
      **
      **  * Call the %syntax_error function.
      **
      **  * Begin popping the stack until we enter a state where
      **    it is legal to shift the error symbol, then shift
      **    the error symbol.
      **
      **  * Set the error count to three.
      **
      **  * Begin accepting and shifting new tokens.  No new error
      **    processing will occur until three tokens have been
      **    shifted successfully.
      **
      */
      if( yypParser.errcnt<0 ){
        yy_syntax_error(yypParser,yymajor,yyminorunion ,pParse);
      }
      if( yypParser.top().major==93 || yyerrorhit != 0 ){
//#ifndef NDEBUG
        if( yyTraceFILE != null ){
          trace(String.format("%sDiscard input token %s\n",
             yyTracePrompt,yyTokenName[yymajor]).getBytes());
        }
//#endif
        yy_destructor(yymajor,yyminorunion);
        yymajor = 130;
      }else{
         while(
          yypParser.idx >= 0 &&
          yypParser.top().major != 93 &&
          (yyact = yy_find_parser_action(yypParser,93)) >= 305
        ){
          yy_pop_parser_stack(yypParser);
        }
        if( yypParser.idx < 0 || yymajor==0 ){
          yy_destructor(yymajor,yyminorunion);
          yy_parse_failed(yypParser ,pParse);
          yymajor = 130;
        }else if( yypParser.top().major!=93 ){
          YYMINORTYPE u2 = new YYMINORTYPE();
          u2.yy259 = 0;
          yy_shift(yypParser,yyact,93,u2);
        }
      }
      yypParser.errcnt = 3;
      yyerrorhit = 1;
//#else  /* 93 is not defined */
      /* This is what we do if the grammar does not define ERROR:
      **
      **  * Report an error message, and throw away the input token.
      **
      **  * If the input token is $, then fail the parse.
      **
      ** As before, subsequent error messages are suppressed until
      ** three input tokens have been successfully shifted.
      */
//      if( yypParser.errcnt<=0 ){
//        yy_syntax_error(yypParser,yymajor,yyminorunion pParse);
//      }
//      yypParser.errcnt = 3;
//      yy_destructor(yymajor,&yyminorunion);
//      if( yyendofinput ){
//        yy_parse_failed(yypParser pParse);
//      }
//      yymajor = 130;
//#endif
    }else{
      yy_accept(yypParser ,pParse);
      yymajor = 130;
    }
  }while( yymajor!=130 && yypParser.idx>=0 );
  return;
}
}
