import React, { useContext, useEffect, useState } from 'react';
import { observer } from 'mobx-react';

import ReactMarkdown from 'react-markdown'
import rehypeRaw from 'rehype-raw' // 支持html
import remarkGfm from 'remark-gfm' // 支持删除线、表格、任务列表和 URL
import remarkToc from 'remark-toc' // 支持目录
import rehypeHighlight from 'rehype-highlight' // 支持代码高亮

import './Readme.less';
import './MarkDown.less';
import './Hljs.less';

// md文件
const mdFile = "/gremlin.md"

const Gremlin: React.FC = observer(() => {
  const [mdContent, setMdContent] = useState("")

  // 读取md文件内容
  const getContent = async (url: string) => {
    const res = await fetch(url);
    const content = await res.text();
    setMdContent(content)
  }
  getContent(mdFile)

  return (
    <div className="readme-main">
      <div className="markdown-body">
        <ReactMarkdown rehypePlugins={[rehypeRaw, rehypeHighlight]} remarkPlugins={[remarkGfm, remarkToc]}>{mdContent}</ReactMarkdown>
      </div>
    </div>
  );
});

export default Gremlin;
