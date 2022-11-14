// Copyright 2018 The Flutter team. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:kiddoz_in_flutter/CreateForm.dart';
import 'package:kiddoz_in_flutter/CreatePage.dart';
import 'package:kiddoz_in_flutter/utils/Repo.dart';
import 'package:provider/provider.dart';

import 'FilterPage.dart';
import 'HomePage.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider(
      create: (_) => Repo(),
      child: MaterialApp(
        theme: ThemeData(
            textTheme: Theme.of(context).textTheme.apply(
                  bodyColor: Colors.black,
                )),
        home: CupertinoTabScaffold(
          tabBuilder: (context, index) {
            return const [HomePage(), CreatePage(), FilterPage()][index];
          },
          tabBar: CupertinoTabBar(
            items: const <BottomNavigationBarItem>[
              BottomNavigationBarItem(
                icon: Icon(Icons.home),
                label: 'Home',
              ),
              BottomNavigationBarItem(
                icon: Icon(Icons.add),
                label: 'Create',
              ),
              BottomNavigationBarItem(
                icon: Icon(Icons.filter),
                label: 'Filter',
              ),
            ],
          ),
        ),
      ),
    );
  }
}
