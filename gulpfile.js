/*
 * Copyright (c) 2012-2016, b3log.org & hacpai.com & fangstar.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @file frontend tool.
 * 
 * @author <a href="mailto:liliyuan@fangstar.net">Liyuan Li</a>
 * @version 0.1.0.0, Jan 29, 2016 
 */
var gulp = require("gulp");
var concat = require('gulp-concat');
var minifyCSS = require('gulp-minify-css');
var uglify = require('gulp-uglify');
var sourcemaps = require("gulp-sourcemaps");

gulp.task('cc', function () {
    // css
    // var cssLibs = ['./static/js/lib/jquery-layout/layout-default-latest.css',
    //     './static/js/lib/codemirror-5.1/codemirror.css',
    //     './static/js/lib/codemirror-5.1/addon/hint/show-hint.css',
    //     './static/js/lib/codemirror-5.1/addon/lint/lint.css',
    //     './static/js/lib/codemirror-5.1/addon/fold/foldgutter.css',
    //     './static/js/lib/codemirror-5.1/addon/dialog/dialog.css',
    //     './static/js/overwrite/codemirror/theme/*.css'];
    // gulp.src(cssLibs)
    //         .pipe(minifyCSS())
    //         .pipe(concat('lib.min.css'))
    //         .pipe(gulp.dest('./static/css/'));


    // js
    var jsJqueryUpload = ['./src/main/webapp/js/lib/jquery/file-upload-9.10.1/vendor/jquery.ui.widget.js',
        './src/main/webapp/js/lib/jquery/file-upload-9.10.1/jquery.iframe-transport.js',
        './src/main/webapp/js/lib/jquery/file-upload-9.10.1/jquery.fileupload.js',
        './src/main/webapp/js/lib/jquery/file-upload-9.10.1/jquery.fileupload-process.js',
        './src/main/webapp/js/lib/jquery/file-upload-9.10.1/jquery.fileupload-validate.js'];
    gulp.src(jsJqueryUpload)
            .pipe(uglify())
            .pipe(concat('jquery.fileupload.min.js'))
            .pipe(gulp.dest('./src/main/webapp/js/lib/jquery/file-upload-9.10.1/'));

    var jsCodemirror = ['./src/main/webapp/js/lib/editor/codemirror.js',
        './src/main/webapp/js/lib/editor/fullscreen.js',
        './src/main/webapp/js/lib/editor/placeholder.js',
        './src/main/webapp/js/overwrite/codemirror/addon/hint/show-hint.js'];
    gulp.src(jsCodemirror)
            .pipe(uglify())
            .pipe(concat('codemirror.min.js'))
            .pipe(gulp.dest('./src/main/webapp/js/lib/editor/'));

});